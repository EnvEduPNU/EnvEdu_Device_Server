//package com.example.dynamodbtest.user.dto;
//
//import lombok.Getter;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.annotation.Transient;
//import org.springframework.data.relational.core.mapping.Column;
//import org.springframework.data.relational.core.mapping.Table;
//import lombok.Setter;
//import lombok.NoArgsConstructor;
//
//@Table("student_educator")
//@Getter
//@Setter
//@NoArgsConstructor
//public class Student_Educator {
//
//    @Id
//    private Long idStudent_Educator;
//
//    @Column("student_id")
//    private Long studentId;
//
//    @Column("educator_id")
//    private Long educatorId;
//
//    @Transient
//    private Student student;
//
//    @Transient
//    private Educator educator;
//
//    public Student_Educator(Long studentId, Long educatorId) {
//        this.studentId = studentId;
//        this.educatorId = educatorId;
//    }
//
//    public static Student_Educator of(Student student, Educator educator) {
//        Student_Educator studentEducator = new Student_Educator(student.getIdStudent(), educator.getIdEducator());
//        studentEducator.setStudent(student);
//        studentEducator.setEducator(educator);
//        return studentEducator;
//    }
//}
