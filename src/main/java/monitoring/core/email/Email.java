package monitoring.core.email;
//package com.mkyong.common;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Email
{
    public static void main( String[] args )
    {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("Spring-Mail.xml");

        EmailSender mm = (EmailSender) context.getBean("emailUtil");
        mm.sendMail("adaptivemonitoringsystem@gmail.com",
                "vaseekarangowthamy@gmail.com",
                "System Alert",
                "Testing only \n\n Hello Spring Email Sender");

    }
}