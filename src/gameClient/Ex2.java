package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

public class Ex2 implements Runnable{

    private static MyFrame _win;
    private static LoginPage login;
    private static Arena _ar;

    private static game_service game;
    private static directed_weighted_graph graph;
    private static dw_graph_algorithms _algo;

    private static HashMap<CL_Pokemon, Boolean> lock_p;
    private static HashMap<Integer, List<node_data>> paths;
    private static PriorityQueue<CL_Agent> ags;

    private static boolean flag;

    private static final boolean is_Test = false; // set true for testing. -> control frame title.
    private static final int NUM_OF_LEVELS = 24;
    private static Integer level;
    private static Long id;
    private static final int dt = 100;
    private static int c;

    /**
     *
     * user can choose to run the game manually to enter ID and level
     * or with arguments
     * where a[0] = id, a[1] = level
     * id => consists of 9 numbers
     * level => int >= 0
     * @param a
     * @throws InterruptedException
     */

    public static void main(String[] a) throws InterruptedException {

        if (a.length > 0) {

            level = Integer.parseInt(a[1]);
            id = Long.parseLong(a[0]);

            Thread client = new Thread(new Ex2());
            client.start();
            client.join();

        }
        else {

            login = new LoginPage("Welcome to Pokemons game.");

            boolean start = login.getStatus();

            while (!start) {

                start = login.getStatus();

                Thread.sleep(100);

                if(start) {

                    level = login.getLevel();
                    id = login.getID();

                }
            }

            Thread client = new Thread(new Ex2());
            client.start();
            client.join();
        }


    }

    @Override
    public void run() {
        _ar = new Arena();
        _algo = new DWGraph_Algo();

        game = Game_Server_Ex2.getServer(level);

        if (id != null && level != null) {
            System.out.println("User ID: " + id);
            System.out.println("level: " + level);

            game.login(id);
        }
        else {
            System.out.println("could not get level or id read.\nPlease try again.. Closing.");
            System.exit(0);
        }

        load_graph(game.toString());
        graph = _algo.getGraph();

        _ar.setGraph(graph);

        init_game();

        game.startGame();
        _ar.setAgents(Arena.getAgents(game.getAgents(), graph));

        ags = new PriorityQueue<>((o1, o2) -> Double.compare(o2.getSpeed(), o1.getSpeed()));

        for (CL_Agent a : _ar.getAgents()) {
            ags.add(a);
            paths.put(a.getID(), new ArrayList<>());
        }

        _win.setTitle("Ex2 " + game.toString());

        while(game.isRunning()) {

            moveAgants(game, graph);

            List<String> info = new ArrayList<>();

            for (CL_Agent agent : _ar.getAgents()){
                info.add("Agent_n: " + agent.getID() + ", Value: " + agent.getValue() + ", speed: " + agent.getSpeed()
                + ", is Moving: " + agent.isMoving());
            }

            info.add("TTE: " + game.timeToEnd() + " miliseconds");

            _ar.set_info(info);

            try {

                _win.repaint();
                Thread.sleep(dt);

            }
            catch(Exception e) {
                e.printStackTrace();
            }

        }

        String res = game.toString();
        System.out.println("level: " + level + ", res: " + res);

        System.exit(0);
    }

    /**
     * Uses algo load to load a graph with a give game info
     * @param game_info
     */
    private void load_graph(String game_info) {

        try {
            JSONObject jo = new JSONObject(game_info);

            JSONObject game = jo.getJSONObject("GameServer");

            String s = game.toString();

            String filepath = s.substring(s.indexOf("data/"), s.indexOf("\",\"agents"));


            _algo.load(filepath);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * helper function to initiate and set arena,
     * and information related to pokemons and agents.
     *
     */
    private void init_game() {
        String fruit_list = game.getPokemons();

        lock_p = new HashMap<>();
        paths = new HashMap<>();

        c = 0;

        ArrayList<CL_Pokemon> fruits = Arena.json2Pokemons(fruit_list);

        _ar.setPokemons(fruits);

        if (is_Test)
            _win = new MyFrame("Ex2 Test");
        else
            _win = new MyFrame("Ex2");

        _win.setSize(1000, 1000);
        _win.update(_ar);
        _win.show();

        String info = game.toString();
        JSONObject line;

        try {
            line = new JSONObject(info);
            int rs = line.getJSONObject("GameServer").getInt("agents");

            System.out.println("Game info: " + info);
            System.out.println("Pokemons: " + fruit_list);


            PriorityQueue<Double> vals = new PriorityQueue<>((x, y) -> Double.compare(y, x));
            HashMap<Double, CL_Pokemon> acc = new HashMap<>();

            for (CL_Pokemon fruit : fruits) {

                lock_p.put(fruit, false);
                vals.add(fruit.getValue());
                acc.put(fruit.getValue(), fruit);
            }

            for(int a = 0; a < rs && !vals.isEmpty(); a++) {

                double val = vals.poll();

                CL_Pokemon f = acc.get(val);

                int nn = f.get_edge().getSrc();

                game.addAgent(nn);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Moves each of the agents along the edge,
     * in case the agent is on a node, the next destination (next edge) is chosen
     * via djikstra algorithm combined with fruit's value as priority.
     * @param game
     * @param gg
     * @param
     */
    private static void moveAgants(game_service game, directed_weighted_graph gg) {

        String lg = game.move();

        List<CL_Agent> log = Arena.getAgents(lg, gg);

        List<CL_Pokemon> poks = Arena.json2Pokemons(game.getPokemons());

        _ar.setPokemons(poks);
        _ar.setAgents(log);

        for (CL_Agent ag : log) {

            int id = ag.getID();
            int dest = ag.getNextNode();

            if (dest == -1) {

                c++;

                if (paths.get(id).isEmpty() || paths.get(id).size() == 1) {
//                    ags.addAll(log);
//                    findBestNode(ag, poks, log);

                    flag = true;
                }

                if (c >= 0 && flag) {
                    c = 0;
                    flag = false;

                    ags.addAll(log);
                    findBestNode(ag, poks, log);
                }
                else {
                    if (paths.get(id).size() > 1) {
                        game.chooseNextEdge(ag.getID(), paths.get(id).remove(1).getKey());
                    }
                }

//                int dest_ = paths.get(id).remove(1).getKey(); // index 0 => src node.
//
//                game.chooseNextEdge(ag.getID(), dest_);
//
//                if (f) System.out.println("Agent: " + id + ", val: " + v + "   turned to node: " + dest_);
//                else System.out.println("Agent's " + id + " Remaining path: " + paths.get(id) + ", turned to node: " + dest_);
            }
        }
    }

    /**
     * using djikstra to find shortest path
     * between src and dest to eat a fruit.
     *
     * first the agent who triggered the first movement
     * will aim towards the most valueable fruit.
     * then the rest of the agents (if any) are set to find the next
     * most valuable fruit and seek it.
     *
     * this function is triggered everytime an Agents eats a fruit, since
     * a new fruit is immediately spawned after a fruit is eaten, therefore
     * all the other agents should know if their previous preferred path
     * is still as such.
     *
     * @param agent
     * @param agents
     * @param fruits
     *
     */
    private static void findBestNode(CL_Agent agent, List<CL_Pokemon> fruits, List<CL_Agent> agents) {

//        lock_p.clear();

        for (CL_Pokemon f : fruits) {
            lock_p.put(f, false);
        }
        
        PriorityQueue<Double> q = new PriorityQueue<>((x, y) -> Double.compare(y, x));
        HashMap<Double, CL_Pokemon> temp = new HashMap<>();

        for (CL_Pokemon pok : fruits) {

            double d = _algo.shortestPathDist(agent.getSrcNode(), pok.get_edge().getSrc());

            d = Math.pow((pok.getValue() * agent.getSpeed() / d), 2);

            q.add(d);
            temp.put(d, pok);

        }

        CL_Pokemon f = temp.get(q.poll());
        find_path(agent, f);

        lock_p.put(f, true);

        q.clear();
        temp.clear();

        while (!ags.isEmpty()) {
            CL_Agent ag = ags.poll();

            if (ag.getID() != agent.getID()) {

                for (CL_Pokemon pok : fruits) {

                    if (!lock_p.get(pok)) {

                        double d = _algo.shortestPathDist(ag.getSrcNode(), pok.get_edge().getSrc());

                        d = Math.pow((pok.getValue() * ag.getSpeed() / d), 2);

                        q.add(d);
                        temp.put(d, pok);
                    }
                }

                CL_Pokemon t = temp.get(q.poll());

                find_path(ag, t);
                lock_p.put(t, true);

            }

        }

    }

    /**
     * helper function to fill an Agent's path.
     * @param agent
     * @param f
     */
    private static void find_path(CL_Agent agent, CL_Pokemon f) {

        List<node_data> l = _algo.shortestPath(agent.getSrcNode(), f.get_edge().getSrc());
        l.add(graph.getNode(f.get_edge().getDest()));

        System.out.print("Agent:" +agent.getID() + ", path: ");
        for (node_data n : l)
            System.out.print(n.getKey()+"->");
        System.out.println();

        paths.put(agent.getID(), l);

    }

}