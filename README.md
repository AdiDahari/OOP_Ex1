# ex1 - OOP
## weighted_graph
this interface is imlemented by WGraph_DS.
each graph contains:
1. graph - HashMap<Integer, node_info> - map contains all nodes of the graph by their keys.
2. edges - HashMap<Integer, HashMap<Integer, Edge>> -

   a. main map - each connected node of the graph has a map.
   
   b. nested map - each nested map contains an Edge ( described below ) referred by destination nodes key.
3. edgeCount - int - count of the number of edges in graph.
4. modeCount - int - count of the number of changes made in graph.

### Nested Classes:
#### node_info:
this interface is implemented by class NodeInfo - nested in WGraph_DS class.
each node contains:
1. Key - int - unique for each node created.
2. Info - String - contains nodes info.
3. Tag - double - used in Dijkstra Algorithm ( DIJKSTRA class ).
#### Edge(Additional Nested Class):
this is a nested class made to keep each connection with a weight value.
each edge contains:
1. _node - node_info - destination node of the edge.
2. _dst - int - the key of destination node.
3. _weight - double - weight of the edge, "price" of going through this edge.

## weighted_graph_algorithms
This interface is implemented by WGraph_Algo,
it uses an additionally made class called DIJKSTRA that contains implementation of 2 method based on Dijkstra Algorithm and BFS.
this class initialize a weighted_graph to apply several method on it.
### Method implemented:
#### init:
A method that initializes a given weighted_graph to this class.
#### getGraph:
This method lets changing the map initialized without directly referring to it. returns the underlying graph of the class.
#### copy:
This is a deep-copy method that returns a deep copy of the underlying graph of the class. uses copy constructors made in WGraph_DS.
#### isConnected:
This method checks whether the whole graph is connected or not. connected graph = each node in the graph has a path to every other node.
this method is imported from ex0 with a few minor changes to fit the weighted_graph implementation.
for further information check exo: https://github.com/AdiDahari/OOP_ex0.
#### shortestPathDist:
This method finds the lowest-weighted path between 2 nodes in graph and return its weight,
by using dijkstraPath implementation of DIJKSTRA class.
before calling the algorithm, a few initial conditions are being checked:
   1. existance of given keys - if one of the keys isn't in graph, or if graph's size is 0 - no possible path - returns -1.
   2. equality of the given keys - if both keys are equal returns 0.0, as the path between a node and itself is weightless.

resource: a video that helped me understanding the idea of the algorithm: https://www.youtube.com/watch?v=FSm1zybd0Tk.

#### shortestPath:
This method returns an ordered List of nodes which are the path from noe key to the other,
by using dijkstraPath implementation of DIJKSTRA class.
before calling the algorithm, a few initial conditions are being checked:
   1. existance of given keys - if one of the keys isn't in graph, or if graph's size is 0 - no possible path - returns null.
   2. equality of the given keys - if both keys are equal returns an empty List as the path between a node and itself is an empty List.
   
resource: a video that helped me understanding the idea of the algorithm: https://www.youtube.com/watch?v=FSm1zybd0Tk.

#### save:
This method saves the underlying graph of the class to a file with a given name & path,
using the File/Object Output Stream classes as shown in TA4 (Simon).
in order to prevent running into problems with unexisting destination folder, a new file is being created and deleted before saving the graph itself,
and a check for its parent folders is made - if any of these doesn't exist - creating it (idea taken from TA3 - Shai Aharon).

#### load:
This method is loading a graph file to the underlying graph of the class,
using the File/Object Input Stream classes as shown in TA4 (Simon).
if any problem occurs during the the loading proccess - no changes to the current underlying graph.

## DIJKSTRA(Additional class):
This is an additionally made class containing 2 algorithm based on BFS and Dijkstra Algorithm.
### Method implemented:
#### dijkstraPath:
A method based on Dijkstra Algorithm.
this method going through every path possible and initializin evey node in path by a Tag representing the weight of the path to it.
the implementation makes it possible for a node's tag to be updated if found a lower-weighted path to it, in that way it is promised that each node's tag will
contain the lowest-weighted path possible to it from the start node.
it does so by using 3 different data structures: 2*HashMap, 1*PriorityQueue.
one HashMap contains boolean values - for marking visited nodes.
second HashMap contains node_info - for implementing the returned by going backwards.
PriorityQueue with TagComparator - for handling the nodes in a logical way.
#### bfsConnection:
this BFS-based algorithm uses boolean HashMap for marking visited nodes. it uses an Queue, insereted by the visited nodes and used to get all of node's neighbors. this method checks every possible path from a start node, by that assuring no node which have a connection to the start node isn't being checked. in the end of this method it checks the size of the boolean map against the praph's nodeSize, if the size is equal - all nodes are connected. else - not all nodes are connected and the boolean value "false" is returned. ( imported from ex0 and changed lightly to work with the new implementation of weighted_graph).
