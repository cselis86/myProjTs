package com.elis.salesmen;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;




public class Main {

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

    // shortest rout display
    public static ArrayList<city> shortestRoutDisplay(ArrayList<ArrayList<city>> routs, int[][] map){
        ArrayList<city> rout = new ArrayList<city>();
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
        System.out.println("the shortests rout" );
        System.out.println("--------------------------------------" );

        for (city c:rout){
            System.out.println("City : " + c.city_num);
        }
        System.out.println("Total Distance : " + dist[best_rout]);
        return rout;
    }


    public static void main(String[] args) {
        Random rand = new Random();

        ArrayList<city> cities , dest_map;
        //list of cities
        cities = new ArrayList<city>();

        Scanner sc = new Scanner(System.in);

        //Number of cities
        System.out.println("Enter the number of cities : ");
        int city_n = sc.nextInt();





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

        System.out.println("--------------------------------------" );
        System.out.println("City distance table" );
        System.out.println("--------------------------------------" );
        int[][] city_map= new int[city_n][city_n];

        for(int s=0;s<city_n;s++){
            for(int d=s;d<city_n;d++){
                if (s == d)
                    city_map[s][d]=0;
                else
                    city_map[s][d] = rand.nextInt(100) + 1;
                    city_map[d][s] = city_map [s][d];
            }
        }

        //display distance array
        for(int s=0;s<city_n;s++){
            for(int d=0;d<city_n;d++){
                System.out.print(" " + city_map[s][d]);
            }
            System.out.println();
        }

        //make a route for every starting point

        //create an array for the distance
        int [] dist_count = new int[city_n];


        // Create a list with all the posible routs
        ArrayList<ArrayList<city>> routs = new ArrayList<ArrayList<city>>();

        //create a list for the cities that will be visited
        dest_map = (ArrayList<city>)cities.clone();

        System.out.println("============================================");
        System.out.println("Display routs");
        System.out.println("============================================");
        routDisplay(routs);

        for(city c : cities) {

            //create a list for the cities that will be visited
            dest_map = (ArrayList<city>) cities.clone();

            //create a list for the visited cities
            ArrayList<city> visited = new ArrayList<city>();
            visited.add(c);
            dest_map.remove(c);

            routs.add(spf(dest_map,visited,city_map,c));
        }

        System.out.println("============================================");
        System.out.println("Display routs from SPF");
        System.out.println("============================================");
        routDisplay(routs);

        shortestRoutDisplay(routs, city_map);
    }
}


