package com.elis.salesmen;

import java.util.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class Main {

    //calculate distances

    public static int[][] getMap(int city_n) {
        Random rand = new Random();

        int[][] map = new int[city_n][city_n];

        for(int s=0;s<city_n;s++){
            for(int d=s;d<city_n;d++){
                if (s == d)
                    map[s][d]=0;
                else
                    map[s][d] = rand.nextInt(100) + 1;
               map[d][s] = map [s][d];
            }
        }

        return map;

    }

    //display distances
    public static void showMap(int[][] map, int city_n){


        for(int s=0;s<city_n;s++){
            for(int d=0;d<city_n;d++){
                System.out.print(" " + map[s][d]);
            }
            System.out.println();
        }
    }

    //display all routs
    public static void routDisplay(ArrayList<ArrayList<city>> routs){
        ArrayList<city> cityArrayList = new ArrayList<city>();
        for (int i=0;i<routs.size();i++){
            System.out.println("Rout no. : " + i);
            for (city c:routs.get(i)){
                System.out.print("City : [" + c.city_num + "]");
            }
            System.out.println(" ");
        }
    }


    //find the closest neighbor
    public static city near_neighbor(ArrayList<city> cities, city start, int[][] map){
        city dest=start;
        int min_dist = 101;
        for (city c:cities){
            if(map[start.city_num][c.city_num]<min_dist){
                min_dist = map[start.city_num][c.city_num];
                dest = c;
            }
        }
        return dest;
    }



    //calculate the distance of rout
    public static int routDistance(ArrayList<city> destination_map, int[][] map){
        int distance=0;
        for(int j=0;j<destination_map.size()-1;j++){
            distance+=map[destination_map.get(j).city_num][destination_map.get(j+1).city_num];
        }
        return distance;
    }



    //shortest path  search (greedy)
    public static ArrayList<city> spf(ArrayList<city> destination , ArrayList<city> visited,int[][] map,city start){
        city dest=start;
        while(destination.size()>0){
            dest = near_neighbor(destination,start,map);
            destination.remove(dest);
            visited.add(dest);
            start=dest;
        }
        visited.add(visited.get(0));

        return visited;
    }

    //heuristic distance of every node from the target
    private static int[] heuDistance(ArrayList<city> rout, int[][] city_map) {

        int[] distance= new int[rout.size()-1];

        for (int i=0; i<rout.size();i++){
            for (int j=i; j<rout.size()-1;j++){
                distance[i]+=city_map[rout.get(j).city_num][rout.get(j+1).city_num];
            }
        }

        return distance;
    }

    //A* implementation given the posible destination(destmap), the map of distances (cityMap), the starting point as a list, and the heurestic distance array (heu_distance)
    public static ArrayList<city> aStar(ArrayList<city> destMap, int[][] cityMap, ArrayList<city> visited, city c, int[] heu_distance) {

        ArrayList<city> destmapT = (ArrayList<city>) destMap.clone();
        //arraylist of the Rout class
        List<Rout> routs = new ArrayList<Rout>();
        Rout r1 = new Rout();

        int f=0;

        //put in the list the initial node f_g=0 f_h=heu_distance[c.num]
        r1.cityRout=visited;
        r1.cityRoutLength = heu_distance[visited.get(0).city_num];
        routs.add(r1);

        //add the leafs of the rout node
        for (city d:destmapT){
            visited.add(d);

            //calculate the func
            f = routDistance(visited,cityMap)+heu_distance[d.city_num];
            r1.cityRout=visited;
            r1.cityRoutLength = f;

            routs.add(r1);

            visited.remove(d);

        }

        //keep expanding the first element until its length is n+1
        while(routs.get(0).cityRout.size()<cityMap[0].length+1){

            destmapT = (ArrayList<city>) destMap.clone();

            visited=routs.get(0).cityRout;

            //remove visited nodes form the availabe destinations
            for(city v : visited){
                if (destmapT.contains(v)){
                    destmapT.remove(v);
                }
            }

           for (city d:destmapT){

               visited.add(d);
               f = routDistance(visited,cityMap)+heu_distance[d.city_num];
               r1.cityRout=visited;
               r1.cityRoutLength=f;
               routs.add(r1);
               visited.remove(d);

              //sort the list of routs
               Collections.sort(routs, new Comparator<Rout>() {
                   @Override
                   public int compare(Rout rout1, Rout rout2) {

                       return rout1.cityRoutLength - rout2.cityRoutLength;
                   }
               });

               //delete the rout with length grater than heuristic distance
               for (Rout r:routs){
                   if (r.cityRoutLength>=heu_distance[0]){
                       routs.remove(r);
                   }
               }
           }
        }
        return routs.get(0).cityRout;
    }



    // shortest Rout display
    public static ArrayList<city> shortestRoutDisplay(ArrayList<ArrayList<city>> routs, int[][] map){
        ArrayList<city> rout;
        int[] dist = new int[routs.size()];
        for(int i=0;i<routs.size();i++){
            rout=routs.get(i);
            for(int j=0;j<rout.size()-1;j++){
                dist[i]+=map[rout.get(j).city_num][rout.get(j+1).city_num];
            }
            dist[i]+= map[rout.get(rout.size()-1).city_num][rout.get(0).city_num];
        }
        int best_rout =0 ;
        int min= dist[0];
        for(int i=0;i<dist.length;i++){
            if (dist[i]<min){
                min=dist[i];
                best_rout = i;
            }
        }

        rout = routs.get(best_rout);

        System.out.println("--------------------------------------" );
        System.out.println("the shortests Rout");
        System.out.println("--------------------------------------" );

        for (city c:rout){
            System.out.println("City : " + c.city_num);
        }
        System.out.println("Total Distance : " + dist[best_rout]);
        return rout;
    }

    //the main function
    public static void main(String[] args) {

        ArrayList<city> cities , dest_map;
        //list of cities
        cities = new ArrayList<city>();

        //create a clone list for the cities that will be visited
        dest_map = (ArrayList<city>) cities.clone();

        Scanner sc = new Scanner(System.in);

        //Number of cities
        System.out.println("Enter the number of cities : ");
        int city_n = sc.nextInt();

        int [][] heuDistance = new int[city_n][city_n];

        System.out.println("--------------------------------------" );
        System.out.println("City list" );
        System.out.println("--------------------------------------" );

        for (int c = 0; c < city_n; c++)
        {
            city ct = new city();
            ct.city_num = c;
            ct.city_name = Integer.toString(c);
            cities.add(ct);
            System.out.println("Hello City! : " + ct.city_num );
        }

        //city distance array
        int[][] city_map= new int[city_n][city_n];
        city_map = getMap(city_n);

        System.out.println("--------------------------------------" );
        System.out.println("City distance table" );
        System.out.println("--------------------------------------" );

        showMap(city_map,city_n);

        // Create a list with all the posible routs
        ArrayList<ArrayList<city>> routs = new ArrayList<ArrayList<city>>();
        ArrayList<ArrayList<city>> routsS = new ArrayList<ArrayList<city>>();




        for(city c : cities) {


            //create a list for the cities that will be visited
            dest_map = (ArrayList<city>) cities.clone();

            //create a list for the visited cities
            ArrayList<city> visited = new ArrayList<city>();

            visited.add(c);
            dest_map.remove(c);

            //calculate the shortest path with spf
            ArrayList<city> rout = spf(dest_map, visited, city_map, c);

            heuDistance[c.city_num]=heuDistance(rout, city_map);

            routs.add(rout);

        }

        for(city c : cities) {


            //create a list for the cities that will be visited
            dest_map = (ArrayList<city>) cities.clone();

            //create a list for the visited cities
            ArrayList<city> visited = new ArrayList<city>();

            ArrayList<city> rout =new ArrayList<city>();

            visited.add(c);
            dest_map.remove(c);

            //calculate rout distance with A*
//            rout = aStar(dest_map, city_map, visited, c, heuDistance[c.city_num]);
//            routsS.add(rout);

        }

        System.out.println("============================================");
        System.out.println("Display routs from SPF");
        System.out.println("============================================");
        routDisplay(routs);
        shortestRoutDisplay(routs, city_map);

        System.out.println("============================================");
        System.out.println("Display routs from A*");
        System.out.println("============================================");
        routDisplay(routsS);


    }




}


