package com.blog.vo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ResultTest {

    @Test
    void testResult() {
        Result res = new Result(200, "Success");
        assertEquals(200, res.getResult());
        assertEquals("Success", res.getMessage());
        
        res.setResult(500);
        res.setMessage("Fail");
        assertEquals(500, res.getResult());
        assertEquals("Fail", res.getMessage());
        
        Result emptyRes = new Result();
        assertNotNull(emptyRes);
    }
}