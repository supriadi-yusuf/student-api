package com.mycompany.api.student.management;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/api/students")
@Validated // for validation of path variable and request parameter
public class StudentApiController {

	private static List<Student> listStudents = new ArrayList<>();
	private static Integer studentID = 0;

	static {
		listStudents.add(new Student(++studentID, "udin"));
		listStudents.add(new Student(++studentID, "sarah"));
	}

	@GetMapping
	public ResponseEntity<?> list(@RequestParam("pageSize") 
			@Min(value=10, message="minimum value of pageSize parameter is 10") 
	        @Max(value=50, message="maximum value of pageSize parameter is 50") Integer pageSize, 
			@Positive(message="pageNum parameter must be positive") Integer pageNum) {
		
		if (listStudents.isEmpty()) {
			return ResponseEntity.noContent().build();
		}

		return new ResponseEntity<>(listStudents, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<Student> add(@RequestBody Student student) {
		student.setId(++studentID);
		listStudents.add(student);

		return new ResponseEntity<Student>(student, HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<?> replace(@RequestBody Student student) {
		if (listStudents.contains(student)) {

			int idx = listStudents.indexOf(student);
			listStudents.set(idx, student);

			return new ResponseEntity<>(student, HttpStatus.OK);
		}

		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?>delete(@PathVariable("id") @Positive(message="id must be positive") Integer id){
		
		Student student = new Student(id);
		
		if (listStudents.contains(student)) {
			listStudents.remove(student);
			
			return ResponseEntity.noContent().build();
		}
		
		return ResponseEntity.notFound().build();
	}

}
