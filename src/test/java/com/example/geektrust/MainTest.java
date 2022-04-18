package com.example.geektrust;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.geektrust.config.Configuration;
import com.example.geektrust.enums.Status;
import com.example.geektrust.service.ExpenseServiceImpl;
import com.example.geektrust.service.IExpenseService;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MainTest
{

    private IExpenseService service;
    private Configuration configuration;

    @BeforeEach
    void setUp()
    {
        this.configuration = new Configuration(new HashMap<>(), new LinkedHashMap<>());
        this.service = new ExpenseServiceImpl(configuration);
        configuration.initialize();
    }

    @Test
    void testInvalidCommand()
    {
        assertEquals(service.execute("BOGUS_CMD"), Status.FAILURE.name());
    }

    @Test
    void testAddMember()
    {
        assertEquals(service.execute("MOVE_IN ANDY"), Status.SUCCESS.name());
    }

    @Test
    void testAddMemberOutsideGroupSizeLimit()
    {
        service.execute("MOVE_IN ANDY");
        service.execute("MOVE_IN WOODY");
        service.execute("MOVE_IN BO");
        assertEquals(service.execute("MOVE_IN REX"), Status.HOUSEFUL.name());
    }

    @Test
    void testSpendAmount()
    {
        service.execute("MOVE_IN ANDY");
        service.execute("MOVE_IN WOODY");
        service.execute("MOVE_IN BO");
        assertEquals(service.execute("SPEND 6000 WOODY ANDY BO"), Status.SUCCESS.name());
    }

    @Test
    void testSpendAmount_AmongBogusMembers()
    {
        service.execute("MOVE_IN ANDY");
        service.execute("MOVE_IN WOODY");
        service.execute("MOVE_IN BO");
        assertEquals(service.execute("SPEND 6000 ANDY REX"), Status.MEMBER_NOT_FOUND.name());
    }

    @Test
    void testDues_BogusMember()
    {
        service.execute("MOVE_IN ANDY");
        service.execute("MOVE_IN WOODY");
        service.execute("MOVE_IN BO");
        assertEquals(service.execute("DUES REX"), Status.MEMBER_NOT_FOUND.name());
    }

    @Test
    void testDues()
    {
        service.execute("MOVE_IN ANDY");
        service.execute("MOVE_IN WOODY");
        service.execute("MOVE_IN BO");
        service.execute("SPEND 6000 WOODY ANDY BO");
        service.execute("SPEND 6000 ANDY BO");
        assertEquals("BO 0\nWOODY 0", service.execute("DUES ANDY"));
    }

    @Test
    void testClearDue_BogusMember()
    {
        service.execute("MOVE_IN ANDY");
        service.execute("MOVE_IN WOODY");
        service.execute("MOVE_IN BO");
        assertEquals(service.execute("CLEAR_DUE BO REX 1000"),
            Status.MEMBER_NOT_FOUND.name());
    }

    @Test
    void testClearDues()
    {
        service.execute("MOVE_IN ANDY");
        service.execute("MOVE_IN WOODY");
        service.execute("MOVE_IN BO");
        service.execute("SPEND 6000 WOODY ANDY BO");
        service.execute("SPEND 6000 ANDY BO");
        assertEquals("0", service.execute("CLEAR_DUE BO ANDY 1000"));
    }

    @Test
    void testClearDues_IncorrectPayment()
    {
        service.execute("MOVE_IN ANDY");
        service.execute("MOVE_IN WOODY");
        service.execute("MOVE_IN BO");
        service.execute("SPEND 6000 WOODY ANDY BO");
        service.execute("SPEND 6000 ANDY BO");
        assertEquals(Status.INCORRECT_PAYMENT.name(), service.execute("CLEAR_DUE BO ANDY 2000"));
    }

    @Test
    void testClearDues_PartialPayment()
    {
        service.execute("MOVE_IN ANDY");
        service.execute("MOVE_IN WOODY");
        service.execute("MOVE_IN BO");
        service.execute("SPEND 6000 WOODY ANDY BO");
        service.execute("SPEND 6000 ANDY BO");
        assertEquals("400", service.execute("CLEAR_DUE BO ANDY 600"));
    }

    @Test
    void testMoveOut_BogusMember()
    {
        service.execute("MOVE_IN ANDY");
        assertEquals(service.execute("MOVE_OUT REX"), Status.MEMBER_NOT_FOUND.name());
    }

    @Test
    void testMoveOut()
    {
        service.execute("MOVE_IN ANDY");
        service.execute("MOVE_IN WOODY");
        service.execute("MOVE_IN BO");
        service.execute("SPEND 6000 WOODY ANDY BO");
        service.execute("SPEND 6000 ANDY BO");
        service.execute("CLEAR_DUE BO ANDY 1000");
        service.execute("CLEAR_DUE BO WOODY 4000");
        assertEquals(service.execute("MOVE_OUT ANDY"), Status.SUCCESS.name());
    }

    @Test
    void testMoveOut_CannotMoveOut()
    {
        service.execute("MOVE_IN ANDY");
        service.execute("MOVE_IN WOODY");
        service.execute("MOVE_IN BO");
        service.execute("SPEND 6000 WOODY ANDY BO");
        service.execute("SPEND 6000 ANDY BO");
        service.execute("CLEAR_DUE BO ANDY 600");
        assertEquals(service.execute("MOVE_OUT ANDY"), Status.FAILURE.name());
    }

    @AfterEach
    void destroy()
    {
        configuration.destroy();
        this.service = null;
    }

}