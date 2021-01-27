import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

class Game{
    String teamName;
    int numOfMatches;
    int numOfWinings;
    int numOfDraws;
    int numOfLost;
    int points;

    int primeniGolovi;
    int dadeniGolovi;


    public static Comparator<Game> compareByPoints = Comparator.comparing(Game::getPoints).thenComparing(Game::getTeamName);


    public Game() {}


    public void setPrimeniGolovi(int primeniGolovi) {
        this.primeniGolovi+= primeniGolovi;
    }

    public void setDadeniGolovi(int dadeniGolovi) {
        this.dadeniGolovi+= dadeniGolovi;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
    public String getTeamName() {
        return teamName;
    }

    public void setNumOfMatches(int numOfMatches) {
        this.numOfMatches+= numOfMatches;
    }

    public void setNumOfWinings(int numOfWinings) {
        this.numOfWinings+= numOfWinings;
    }

    public void setNumOfDraws(int numOfDraws) {
        this.numOfDraws+= numOfDraws;
    }

    public void setNumOfLost(int numOfLost) {
        this.numOfLost+= numOfLost;
    }
    //број_на_победи x 3 + број_на_нерешени x 1
    public int getPoints() {
        return (numOfWinings*3) + (numOfDraws);
    }
    public int golRazlika(){
        return dadeniGolovi-primeniGolovi;
    }

    @Override
    public String toString() {
        // 1. West Ham          10    6    2    2   20
        return String.format("%-15s%5d%5d%5d%5d%5d",teamName,numOfMatches,numOfWinings,numOfDraws,numOfLost,getPoints());

    }
}
class FootballTable{
    HashMap<String, Game> matches;

    public FootballTable() {
        this.matches=new HashMap<>();
    }
    public void addGame(String homeTeam, String awayTeam, int homeGoals, int awayGoals){
        //Bournemouth;   Man Utd;   1;    5
        //Tottenham;   Liverpool;   5;    4
        //Stoke;      Tottenham;    3;    3

        if (homeGoals < awayGoals){
            matches.putIfAbsent(homeTeam,new Game());
            matches.get(homeTeam).setTeamName(homeTeam);

            matches.get(homeTeam).setNumOfMatches(1);
            matches.get(homeTeam).setNumOfWinings(0);
            matches.get(homeTeam).setNumOfDraws(0);
            matches.get(homeTeam).setNumOfLost(1);

            matches.get(homeTeam).setDadeniGolovi(homeGoals);
            matches.get(homeTeam).setPrimeniGolovi(awayGoals);

            matches.putIfAbsent(awayTeam, new Game());
            matches.get(awayTeam).setTeamName(awayTeam);
            matches.get(awayTeam).setNumOfMatches(1);
            matches.get(awayTeam).setNumOfWinings(1);
            matches.get(awayTeam).setNumOfDraws(0);
            matches.get(awayTeam).setNumOfLost(0);

            matches.get(awayTeam).setDadeniGolovi(awayGoals);
            matches.get(awayTeam).setPrimeniGolovi(homeGoals);

        }else if (homeGoals > awayGoals){
            matches.putIfAbsent(homeTeam,new Game());
            matches.get(homeTeam).setTeamName(homeTeam);
            matches.get(homeTeam).setNumOfMatches(1);
            matches.get(homeTeam).setNumOfWinings(1);
            matches.get(homeTeam).setNumOfDraws(0);
            matches.get(homeTeam).setNumOfLost(0);

            matches.get(homeTeam).setDadeniGolovi(homeGoals);
            matches.get(homeTeam).setPrimeniGolovi(awayGoals);

            matches.putIfAbsent(awayTeam, new Game());
            matches.get(awayTeam).setTeamName(awayTeam);
            matches.get(awayTeam).setNumOfMatches(1);
            matches.get(awayTeam).setNumOfWinings(0);
            matches.get(awayTeam).setNumOfDraws(0);
            matches.get(awayTeam).setNumOfLost(1);

            matches.get(awayTeam).setDadeniGolovi(awayGoals);
            matches.get(awayTeam).setPrimeniGolovi(homeGoals);
        }else {
            matches.putIfAbsent(homeTeam,new Game());
            matches.get(homeTeam).setTeamName(homeTeam);
            matches.get(homeTeam).setNumOfMatches(1);
            matches.get(homeTeam).setNumOfWinings(0);
            matches.get(homeTeam).setNumOfDraws(1);
            matches.get(homeTeam).setNumOfLost(0);

            matches.get(homeTeam).setDadeniGolovi(homeGoals);
            matches.get(homeTeam).setPrimeniGolovi(awayGoals);

            matches.putIfAbsent(awayTeam, new Game());
            matches.get(awayTeam).setTeamName(awayTeam);
            matches.get(awayTeam).setNumOfMatches(1);
            matches.get(awayTeam).setNumOfWinings(0);
            matches.get(awayTeam).setNumOfDraws(1);
            matches.get(awayTeam).setNumOfLost(0);

            matches.get(awayTeam).setDadeniGolovi(awayGoals);
            matches.get(awayTeam).setPrimeniGolovi(homeGoals);
        }
    }
    public void printTable(){
        AtomicInteger i= new AtomicInteger(1);
        matches.values()
                .stream()
                .sorted(Comparator
                        .comparing(Game::getPoints).reversed()
                        .thenComparing(Comparator.comparing(Game::golRazlika).reversed())
                        .thenComparing(Game::getTeamName))
                .forEach(game-> System.out.println(String.format("%2d. ", i.getAndIncrement())+game));
    }

}

public class FootballTableTest {
    public static void main(String[] args) throws IOException {
        FootballTable table = new FootballTable();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        reader.lines()
                .map(line -> line.split(";"))
                .forEach(parts -> table.addGame(parts[0], parts[1],
                        Integer.parseInt(parts[2]),
                        Integer.parseInt(parts[3])));
        reader.close();
        System.out.println("=== TABLE ===");
        System.out.printf("%-19s%5s%5s%5s%5s%5s\n", "Team", "P", "W", "D", "L", "PTS");
        table.printTable();
    }
}

// Your code here

