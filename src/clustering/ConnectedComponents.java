// Sample program to find connected components of undirected graph
// http://www.sanfoundry.com/java-program-find-connected-components-undirected-graph/
package clustering;
 
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
class CCGraph
{
    static final int MAXV      = 1000;
    static final int MAXDEGREE = 1000;
    public int       edges[][] = new int[MAXV + 1][MAXDEGREE];
    public int       degree[]  = new int[MAXV + 1];
    public int       nvertices;
    public int       nedges;
 
    CCGraph()
    {
        nvertices = nedges = 0;
        for (int i = 1; i <= MAXV; i++)
            degree[i] = 0;
    }
 
    void read_CCGraph(boolean directed)
    {
        /*int x, y;
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the number of vertices: ");
        nvertices = sc.nextInt();
        System.out.println("Enter the number of edges: ");
        int m = sc.nextInt();
        System.out.println("Enter the edges: <from> <to>");
        for (int i = 1; i <= m; i++)
        {
            x = sc.nextInt();
            y = sc.nextInt();
            insert_edge(x, y, directed);
        }
        sc.close();*/
    	
    	nvertices = 254;
    	int countLine = 0;
        String fileName = "254VisitorsWithDurationMergingSingle.txt", sCurrentLine, splits[];
		BufferedReader br = null;
		try{
			br = new BufferedReader(new FileReader(System.getProperty("user.dir") + "\\" + fileName));
			while ((sCurrentLine = br.readLine()) != null) {
				//if(countLine > 20) break;
				splits = sCurrentLine.split(" ");
				insert_edge(Integer.parseInt(splits[1]), Integer.parseInt(splits[2]), directed);
				countLine++;
				//System.out.println(splits[1]+" "+splits[2] + " = " + extractedDatas[Integer.parseInt(splits[1])] + " " + extractedDatas[Integer.parseInt(splits[2])]);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
    }
 
    void insert_edge(int x, int y, boolean directed)
    {
        if (degree[x] > MAXDEGREE)
            System.out.printf(
                    "Warning: insertion (%d, %d) exceeds max degree\n", x, y);
        edges[x][degree[x]] = y;
        degree[x]++;
        if (!directed)
            insert_edge(y, x, true);
        else
            nedges++;
    }
 
    void print_CCGraph()
    {
        for (int i = 0; i < nvertices; i++) //for (int i = 1; i <= nvertices; i++)
        {
            System.out.printf("%d: ", i);
            for (int j = degree[i] - 1; j >= 0; j--)
                System.out.printf(" %d", edges[i][j]);
            System.out.printf("\n");
        }
    }
}
 
public class ConnectedComponents
{
    static final int MAXV         = 1000;
    static boolean   processed[]  = new boolean[MAXV];
    static boolean   discovered[] = new boolean[MAXV];
    static int       parent[]     = new int[MAXV];
 
    static void bfs(CCGraph g, int start)
    {
        Queue<Integer> q = new LinkedList<Integer>();
        int i, v;
        q.offer(start);
        discovered[start] = true;
        while (!q.isEmpty())
        {
            v = q.remove();
            process_vertex(v);
            processed[v] = true;
            for (i = g.degree[v] - 1; i >= 0; i--)
            {
                if (!discovered[g.edges[v][i]])
                {
                    q.offer(g.edges[v][i]);
                    discovered[g.edges[v][i]] = true;
                    parent[g.edges[v][i]] = v;
                }
            }
        }
    }
 
    static void initialize_search(CCGraph g)
    {
        for (int i = 1; i <= g.nvertices; i++)
        {
            processed[i] = discovered[i] = false;
            parent[i] = -1;
        }
    }
 
    static void process_vertex(int v)
    {
        System.out.printf(" %d", v);
    }
 
    static void connected_components(CCGraph g)
    {
        int c;
        initialize_search(g);
        c = 0;
        //for (int i = 1; i <= g.nvertices; i++)
        for(int i=0;i<g.nvertices;i++)
        {
            if (!discovered[i])
            {
                c++;
                System.out.printf("Component %d:", c);
                bfs(g, i);
                System.out.printf("\n");
            }
        }
    }
 
    static public void main(String[] args)
    {
        CCGraph g = new CCGraph();
        g.read_CCGraph(false);
        g.print_CCGraph();
        connected_components(g);
    }
}