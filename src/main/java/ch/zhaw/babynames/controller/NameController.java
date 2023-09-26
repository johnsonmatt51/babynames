package ch.zhaw.babynames.controller;

import ch.zhaw.babynames.model.Name;
import com.opencsv.CSVReader;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@RestController
public class NameController {
    private ArrayList<Name> listOfNames;

    @GetMapping("/names")
    public ArrayList<Name> getNames() {
        return listOfNames;
    }

    @GetMapping("/names/count")
    public long getCount(@RequestParam(required = false) String sex) {
        if (sex != null) {
            return listOfNames.stream().filter(n -> n.getGender().equals(sex)).count();
        } else {
            return listOfNames.size();
        }
    }

    @GetMapping("/names/frequency")
    public long getFrequency(@RequestParam() String name) {
        return listOfNames.stream().filter(n -> n.getName().equals(name)).map(Name::getAmount).findFirst().orElse(0);
    }

    @GetMapping("/names/name")
    public ResponseEntity<List<String>> getNames(@RequestParam() String sex,
                                                 @RequestParam() String start,
                                                 @RequestParam() long length) {
        if (!Objects.equals(sex, "w") && !Objects.equals(sex, "m")) {
            throw new ResponseStatusException(BAD_REQUEST);
        }

        final List<String> result = listOfNames.stream()
                .filter(n -> n.getGender().equals(sex))
                .filter(n -> n.getName().toLowerCase().startsWith(start.toLowerCase()))
                .filter(n -> n.getName().length() == length)
                .map(Name::getName)
                .toList();

        return new ResponseEntity<>(result, OK);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() throws Exception {
        listOfNames = new ArrayList<>();
        Path path = Paths.get(ClassLoader.getSystemResource("data/babynames.csv").toURI());
        System.out.println("Read from: " + path);
        try (Reader reader = Files.newBufferedReader(path)) {
            try (CSVReader csvReader = new CSVReader(reader)) {
                String[] line;
                while ((line = csvReader.readNext()) != null) {
                    listOfNames.add(new Name(line[0], line[1], Integer.parseInt(line[2])));
                }
                System.out.println("Read " + listOfNames.size() + " names");
            }
        }
    }
}
