import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static String path;

    public static void main(String[] args) throws IOException {
        System.out.println("Who do you wish to analyse? (Default = rob)");
        Scanner sc = new Scanner(System.in);
        path = sc.nextLine();
        path = path.equals("") ? "rob" : path;
        try {
            GetMessages getMessages = new GetMessages();
            getMessages.run();
        } catch (NullPointerException e) {
            System.out.println("xdd");
        }

        while (true) {
            try {
                System.out.println("\n------What do you wanna do------\n\n0: Change streamer target\n1: Most messages for single stream\n2: Display message leaderboard\n3: Generate log of all words\n4: Generate log for chatter sent words\n5: Generate log for most sent words\n6: Generate chat log zip for specific user\n7: Log of messages containing word\n-1: Exit\n");

                String userInput = sc.nextLine();

                switch (userInput) {
                    case "0" -> {
                        System.out.println("Enter the streamer (rob/kyo/crump)");
                        path = sc.nextLine();
                    }
                    case "1" -> {
                        System.out.println("Please enter the stream name");
                        String streamNameInput = sc.nextLine();
                        System.out.println("Please enter the top output amount");
                        String top = sc.nextLine();
                        top = top.equals("") ? "15" : top;
                        MostMessages mostMessages = new MostMessages(streamNameInput, Integer.parseInt(top));
                        mostMessages.run();
                    }

                    case "2" -> {
                        System.out.println("How many results:");
                        String top = sc.nextLine();
                        top = top.equals("") ? "20" : top;
                        System.out.println("Enter username:");
                        String username = sc.nextLine();
                        username = username.equals("") ? "alphasalami" : username;
                        System.out.println("Enter position:");
                        String position = sc.nextLine();
                        position = position.equals("") ? "69" : position;
                        GenerateLeaderboard leaderboard = new GenerateLeaderboard(Integer.parseInt(top), username, Integer.parseInt(position));
                        leaderboard.run();
                    }

                    case "3" -> {
                        GetWords.run();
                    }

                    case "4" -> {
                        System.out.println("Please enter the chatter username");
                        String targetUserInput = sc.nextLine();
                        ChatterWords chatterWords = new ChatterWords(targetUserInput);
                        chatterWords.run();
                    }

                    case "5" -> {
                        System.out.println("Please enter the word");
                        String targetWordInput = sc.nextLine();
                        CompareWordCount compareWordCount = new CompareWordCount(targetWordInput);
                        compareWordCount.run();
                    }

                    case "6" -> {
                        System.out.println("Please enter chatter name");
                        String targetChatter = sc.nextLine();
                        MakeZip makeZip = new MakeZip(targetChatter);
                        makeZip.run();
                    }

                    case "7" -> {
                        System.out.println("Please enter chatter name");
                        String targetUsernameInput = sc.nextLine();
                        System.out.println("Please enter the word");
                        String targetWordInput = sc.nextLine();
                        CheckWordContains checkWordContains = new CheckWordContains(targetUsernameInput, targetWordInput);
                        checkWordContains.run();
                    }
                    case "-1" -> {
                        return;
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}

