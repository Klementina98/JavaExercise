import java.util.*;
import java.util.stream.Collectors;


class Names{
    HashMap<String, Integer> firstMap;
    List<String> prints;
    HashMap<Integer, HashMap<String,Set<Character>>> secondMap;

    public Names() {
        this.firstMap=new HashMap<>();
        this.secondMap=new HashMap<>();
    }
    public void addName(String name){
        //firstMap.putIfAbsent(name,1);
        if (firstMap.containsKey(name)){
            firstMap.put(name,firstMap.get(name)+1);
        }else{
            firstMap.put(name,1);
        }
    }
    public void printN(int n){
        reversed();
        prints = new ArrayList<>();
        secondMap.keySet()
                .stream()
                .filter(key->key>=n)
                .forEach(key->secondMap.get(key).keySet().forEach(name->{
                    //tuka e kluchot name Filip
                    int count=secondMap.get(key).get(name).size();
                    prints.add(String.format("%s (%d) %d",name,key,count));
//                    System.out.println(String.format("%s (%d) %d",name,key,count));
                }));

        prints.sort(Comparator.naturalOrder());

        for (String s:prints){
            System.out.println(s);
        }

    }

    private void reversed() {
        //sega sum sigurna deka prvataMapa mi e polna
        //sega trebe da ja ispolnam vtorata mapa
        firstMap.keySet().forEach(name->{
            Integer key = firstMap.get(name);
            secondMap.putIfAbsent(key,new HashMap<>());
            Set<Character> set=new HashSet<>();
            char[] karakteri=name.toLowerCase().toCharArray();
            for (char e:karakteri){
                set.add(e);
            }
            secondMap.get(key).put(name,set);
        });
    }
    /*public String findName(int len, int x){
        List<String> listNames=prints.stream().map(s->s.split(" ")[0])
                        .filter(name->name.length()<len).collect(Collectors.toList());
        int position = x % listNames.size();
        return listNames.get(position);
    }*/
    public String findName(int len, int x) {
        List<String> lista = firstMap.keySet().stream().filter(name -> name.length() < len).sorted().collect(Collectors.toList());
        return lista.get(x % lista.size());
    }



}

public class NamesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        Names names = new Names();
        for (int i = 0; i < n; ++i) {
            String name = scanner.nextLine();
            names.addName(name);
        }
        n = scanner.nextInt();
        System.out.printf("===== PRINT NAMES APPEARING AT LEAST %d TIMES =====\n", n);
        names.printN(n);
        System.out.println("===== FIND NAME =====");
        int len = scanner.nextInt();
        int index = scanner.nextInt();
        System.out.println(names.findName(len, index));
        scanner.close();

    }
}

// vashiot kod ovde