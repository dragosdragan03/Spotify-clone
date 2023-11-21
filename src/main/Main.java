package main;

import checker.Checker;
import checker.CheckerConstants;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import commands.Command;
import commands.CommandExecute;
import commands.UserHistory;
import commands.search.SearchBar;
import commands.search.Select;
import fileio.input.LibraryInput;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;

/**
 * The entry point to this homework. It runs the checker that tests your implentation.
 */
public final class Main {
    static final String LIBRARY_PATH = CheckerConstants.TESTS_PATH + "library/library.json";

    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * DO NOT MODIFY MAIN METHOD
     * Call the checker
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(CheckerConstants.TESTS_PATH);
        Path path = Paths.get(CheckerConstants.RESULT_PATH);

        if (Files.exists(path)) {
            File resultFile = new File(String.valueOf(path));
            for (File file : Objects.requireNonNull(resultFile.listFiles())) {
                file.delete();
            }
            resultFile.delete();
        }
        Files.createDirectories(path);

        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.getName().startsWith("library")) {
                continue;
            }

            String filepath = CheckerConstants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getName(), filepath);
            }
        }

        Checker.calculateScore();
    }

    /**
     * @param filePathInput for input file
     * @param filePathOutput for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePathInput,
                              final String filePathOutput) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        LibraryInput library = objectMapper.readValue(new File(LIBRARY_PATH), LibraryInput.class); // este o baza de date
        CommandExecute.clear();
        ArrayList<Command> commands = objectMapper.readValue(new File(CheckerConstants.TESTS_PATH + filePathInput), new TypeReference<ArrayList<Command>>() {
        }); // imi intra automat in clasa
//        ArrayList<Command> commands = objectMapper.readValue(new File(CheckerConstants.TESTS_PATH + "test01_searchBar_songs_podcasts.json"), new TypeReference<ArrayList<Command>>() {
//        });
//        ArrayList<Command> commands = objectMapper.readValue(new File(CheckerConstants.TESTS_PATH + "test02_playPause_song.json"), new TypeReference<ArrayList<Command>>() {
//        });
//        ArrayList<Command> commands = objectMapper.readValue(new File(CheckerConstants.TESTS_PATH + "test03_like_create_addRemove.json"), new TypeReference<ArrayList<Command>>() {
//        });
//        ArrayList<Command> commands = objectMapper.readValue(new File(CheckerConstants.TESTS_PATH + "test04_like_create_addRemove_error.json"), new TypeReference<ArrayList<Command>>() {
//        });
//        ArrayList<Command> commands = objectMapper.readValue(new File(CheckerConstants.TESTS_PATH + "test05_playPause_playlist_podcast.json"), new TypeReference<ArrayList<Command>>() {
//        });
//        ArrayList<Command> commands = objectMapper.readValue(new File(CheckerConstants.TESTS_PATH + "test08_repeat_error.json"), new TypeReference<ArrayList<Command>>() {
//        });
        ArrayNode outputs = objectMapper.createArrayNode();
        for (int i = 0; i < commands.size(); i++) {
            CommandExecute commandExecute = new CommandExecute(commands.get(i), library);
            JsonNode jsonString = objectMapper.valueToTree(commandExecute.executeCommand(commands.get(i)));
            outputs.add(jsonString);
        }
//
//            UserHistory userHistory = new UserHistory();
//            JsonNode newjsonString = objectMapper.valueToTree(userHistory);
//            outputs.add(newjsonString);

//            SearchBar search = new SearchBar(commands.get(0), library, commands.get(0).getType(), commands.get(0).getFilters());
//            CommandExecute commandExecute = new CommandExecute(commands.get(0), library);
//            JsonNode jsonString = objectMapper.valueToTree(commandExecute.executeCommand(commands.get(0)));
//        JsonNode jsonString = objectMapper.valueToTree(commandExecute);
//        JsonNode jsonString = objectMapper.valueToTree(search);
//            outputs.add(jsonString);
        // TODO add your implementation


        // trebuie sa transform json de input in obiect java cu objectMapper
        // trebuie sa l transform inapoi in json ca sa l afisez
        // trebuie sa verific outputul

//        library.getPodcasts();

        //String json = "{ \"color\" : \"Black\", \"type\" : \"BMW\" }";
        //Car car = objectMapper.readValue(json, Car.class);

        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(filePathOutput), outputs);
    }
}
