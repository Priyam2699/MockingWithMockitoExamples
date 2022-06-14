package com.luv2code.test;


import com.luv2code.component.MvcTestingExampleApplication;
import com.luv2code.component.dao.ApplicationDao;
import com.luv2code.component.models.CollegeStudent;
import com.luv2code.component.models.StudentGrades;
import com.luv2code.component.service.ApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;

import org.junit.jupiter.api.*;
import org.springframework.test.util.AssertionErrors;

import static org.mockito.Mockito.*;

@SpringBootTest(classes = MvcTestingExampleApplication.class)
public class MockAnnotationTest {


    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    CollegeStudent studentOne;

    @Autowired
    StudentGrades studentGrades;

    @MockBean
    private ApplicationDao applicationDao;

    @Autowired
    private ApplicationService applicationService;


    @BeforeEach
    public void setup()
    {
        studentOne.setFirstname("Priyam");
        studentOne.setLastname("Dua");
        studentOne.setEmailAddress("priyamdua26@gmail.com");
        studentOne.setStudentGrades(studentGrades);

    }


    @DisplayName("When and Verify")
    @Test
    public void asserEqualsTestAndGrades()
    {
        when(applicationDao.addGradeResultsForSingleClass(studentGrades.getMathGradeResults())).thenReturn(100.00);

        Assertions.assertEquals(100,applicationService.addGradeResultsForSingleClass(studentOne.getStudentGrades().getMathGradeResults()));

       verify(applicationDao).addGradeResultsForSingleClass(studentGrades.getMathGradeResults());

       verify(applicationDao,times(1)).addGradeResultsForSingleClass(studentGrades.getMathGradeResults());

    }

    @DisplayName("Find GPA")
    @Test
    public void assertEqualsTestFindGPA()
    {

        when(applicationDao.findGradePointAverage(studentGrades.getMathGradeResults())).thenReturn(88.31);

        Assertions.assertEquals(88.31,applicationService.findGradePointAverage(studentOne.getStudentGrades().getMathGradeResults()));
    }

    @DisplayName("Not null")
    @Test
    public void testAssertNotNull()
    {
        when(applicationDao.checkNull(studentGrades.getMathGradeResults())).thenReturn(true);

        Assertions.assertNotNull(applicationService.checkNull(studentOne.getStudentGrades().getMathGradeResults()),"objects should not be null");
    }


    @DisplayName("Throw RunTime Exception")
    @Test
    public void throwRunTime()
    {
        CollegeStudent nullStudent = (CollegeStudent) applicationContext.getBean("collegeStudent");

        doThrow(new RuntimeException()).when(applicationDao).checkNull(nullStudent);

        Assertions.assertThrows(RuntimeException.class,() -> {applicationService.checkNull(nullStudent);} );

        verify(applicationDao, times(1)).checkNull(nullStudent);


    }



    @DisplayName("Multiple Stubbing")
    @Test
    public void stubbingConsecutiveCalls() {
        {
            CollegeStudent nullstudent  = (CollegeStudent)  applicationContext.getBean("collegeStudent");

            when(applicationDao.checkNull(nullstudent)).thenThrow(new RuntimeException()).thenReturn("Do not throw exception second time");

            Assertions.assertThrows(RuntimeException.class, () ->{applicationService.checkNull(nullstudent);});


            Assertions.assertEquals("Do not throw exception second time", applicationService.checkNull(nullstudent));

            verify(applicationDao, times(2)).checkNull(nullstudent);
        }
    }


}
