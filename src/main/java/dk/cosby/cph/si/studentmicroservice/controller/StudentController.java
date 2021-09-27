package dk.cosby.cph.si.studentmicroservice.controller;

// You need to import hateoas, also as Maven dependency
// Spring Boot can do this for you

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import dk.cosby.cph.si.studentmicroservice.exception.StudentNotFoundException;
import dk.cosby.cph.si.studentmicroservice.model.Students;
import dk.cosby.cph.si.studentmicroservice.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    StudentRepository repo;

    @GetMapping("/all")
    public CollectionModel<EntityModel<Students>> retrieveAllStudents()
    {
        CollectionModel<Students> studentCollection = CollectionModel.of(repo.findAll());
        List<EntityModel<Students>> studentList = repo.findAll().stream().map(student ->
                        EntityModel.of(student,
                                linkTo(methodOn(StudentController.class).retrieveStudent(student.getId())).withSelfRel(),
                                linkTo(methodOn(StudentController.class).retrieveAllStudents()).withRel("All Students")))
                .collect(Collectors.toList());

        return CollectionModel.of(studentList, linkTo(methodOn(StudentController.class).retrieveAllStudents()).withSelfRel());
    }

    // This is the only method, which returns hyperlinks, for now
    // If the resource is found, a link to its 'family' is appended to its native load
    @GetMapping("/{id}")
    public EntityModel<Students> retrieveStudent(@PathVariable long id)
    {
        Optional<Students> student = repo.findById(id);
        if (student.isEmpty())
            throw new StudentNotFoundException("id: " + id);

        EntityModel<Students> resource = EntityModel.of(student.get()); 						// get the resource
        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllStudents()); // get link
        resource.add(linkTo.withRel("all-students"));										// append the link

        Link selfLink = linkTo(methodOn(this.getClass()).retrieveStudent(id)).withSelfRel(); //add also link to self
        resource.add(selfLink);
        return resource;
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable long id) {
        repo.deleteById(id);
    }

    // Create a new resource and remember its unique location in the hypermedia
    @PostMapping("/")
    public ResponseEntity<Object> createStudent(@RequestBody Students student)
    {
        Students savedStudent = repo.save(student);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedStudent.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateStudent(@RequestBody Students student, @PathVariable long id)
    {
        Optional<Students> studentOptional = repo.findById(id);
        if (studentOptional.isEmpty())
            return ResponseEntity.notFound().build();
        student.setId(id);
        repo.save(student);
        return ResponseEntity.noContent().build();
    }
}
