package com.company;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {

        Markov m = new Markov();
        try{
            String inputString = new String(Files.readAllBytes(Paths.get("C:\\Users\\argetlam\\IdeaProjects\\Markov\\src\\com\\company\\input.txt")));
            //String inputString = new String(Files.readAllBytes(Paths.get("C:\\Users\\argetlam\\Desktop\\fellowship_of_the_ring.txt")));

            m.feed(inputString, 2);
            m.spew(1000);

        }
        catch (Exception e){System.out.println("Your file could not be read. Check the file path and try again.");}
    }
}
