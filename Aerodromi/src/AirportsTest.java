import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


class Flight{
    String from;
    String to;
    int time;
    int duration;

    public static Comparator<Flight> comparatorByLexyAndTime= Comparator.comparing(Flight::getTo).thenComparing(Flight::departureTime);
    public static Comparator<Flight> comparatorByTime = Comparator.comparing(Flight::departureTime);

    public Flight(String from, String to, int time, int duration) {
        this.from = from;
        this.to = to;
        this.time = time;
        this.duration = duration;
    }

    public String getTo() {
        return to;
    }

    public int getTime() {
        return time; //526
    }

    //LHR-HND 03:28-08:18 4h50m
    public String departureTime(){
        int hours= time/60;
        int minutes= time%60;
        return String.format("%02d:%02d",hours,minutes);
    }
    public String arrivalTime(){
        //12. LHR-JFK 22:43 > 1363 -25:19 +1d 2h36m // 156
        // 25:19 > 1:19
        if(time+duration>1440){
            int newTime = Math.abs(duration+time-1440);
            int hours = newTime / 60;
            int minutes = newTime % 60;
            return String.format("%02d:%02d", hours, minutes);
        }else{
            int timeT = time + duration;
            int hours = timeT / 60;
            int minutes = timeT % 60;
            return String.format("%02d:%02d", hours, minutes);
        }
    }

    public String durationTime(){
        int zbir=time+duration;
        int days=0;
        while (zbir>1440){
            days++;
            zbir-=1440;
        }
        //800

        if (days>0){
            int hours= duration/60;
            int minutes= duration%60;
            return String.format("+%dd %dh%02dm",days,hours,minutes);
        }else {
            int hours= duration/60;
            int minutes= duration%60;
            return String.format("%dh%02dm",hours,minutes);
        }
    }
    @Override
    public String toString() {
        return String.format("%s-%s %s-%s %s",from,to,departureTime(),arrivalTime(),durationTime());
    }
}

class Airport{
    String name;
    String country;
    String code;
    int passengers;


    TreeSet<Flight> flightsByLexiAndTime;
    TreeSet<Flight> flightsByTime;

    public Airport(String name, String country, String code, int passengers) {
        this.name = name;
        this.country = country;
        this.code = code;
        this.passengers = passengers;
        this.flightsByLexiAndTime=new TreeSet<>(Flight.comparatorByLexyAndTime);
        this.flightsByTime = new TreeSet<>(Flight.comparatorByTime);
    }

    @Override
    public String toString() {
       return String.format("%s (%s)\n%s\n%d",name,code,country,passengers);
    }
}


class Airports{

    HashMap<String,Airport> airportHashMap;

    public Airports() {
        airportHashMap=new HashMap<>();
    }

    public void addAirport(String name, String country, String code, int passengers){
        airportHashMap.put(code,new Airport(name,country,code,passengers));
    }
    public void addFlights(String from, String to, int time, int duration){
        airportHashMap.get(from).flightsByLexiAndTime.add(new Flight(from,to,time,duration));
        airportHashMap.get(from).flightsByTime.add(new Flight(from,to,time,duration));
    }
    public void showFlightsFromAirport(String code){
        AtomicInteger i= new AtomicInteger(1);
        System.out.println(airportHashMap.get(code).toString());
        airportHashMap.get(code).flightsByLexiAndTime.stream().forEach(f-> System.out.println(String.format("%d. ", i.getAndIncrement())+f));
    }
    public void showDirectFlightsFromTo(String from, String to){
        List<Flight> flights=airportHashMap.get(from).flightsByTime.stream().filter(f->f.to.equals(to)).collect(Collectors.toList());
        if (flights.size()>0){
            flights.forEach(System.out::println);
        }else{
            System.out.println("No flights from "+from+" to "+to+"");
        }
    }
    public void showDirectFlightsTo(String to){
        TreeSet<Flight> flights=new TreeSet<>(Comparator.comparing(Flight::getTime).thenComparing(Flight::arrivalTime));

        airportHashMap.values().stream().flatMap(a->a.flightsByTime.stream().filter(f->f.to.equals(to))).forEach(flights::add);
        flights.forEach(System.out::println);
    }


}

public class AirportsTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Airports airports = new Airports();
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] codes = new String[n];
        for (int i = 0; i < n; ++i) {
            String al = scanner.nextLine();
            String[] parts = al.split(";");
            airports.addAirport(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
            codes[i] = parts[2];
        }
        int nn = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < nn; ++i) {
            String fl = scanner.nextLine();
            String[] parts = fl.split(";");
            airports.addFlights(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
        }
        int f = scanner.nextInt();
        int t = scanner.nextInt();
        String from = codes[f];
        String to = codes[t];
        System.out.printf("===== FLIGHTS FROM %S =====\n", from);
        airports.showFlightsFromAirport(from);
        System.out.printf("===== DIRECT FLIGHTS FROM %S TO %S =====\n", from, to);
        airports.showDirectFlightsFromTo(from, to);
        t += 5;
        t = t % n;
        to = codes[t];
        System.out.printf("===== DIRECT FLIGHTS TO %S =====\n", to);
        airports.showDirectFlightsTo(to);
    }
}

// vashiot kod ovde

