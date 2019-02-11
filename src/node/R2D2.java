package node;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class R2D2 {
	public char[][] floor;
	public int nodesSearched;
	Node Goal;
	public R2D2(){
		floor = null;
		nodesSearched = 0;
		Goal = null;
	}
	public char[][] generateMap() {
		int x = (int) (Math.random() * 2 + 3);// generate x
		int y = (int) (Math.random() * 2 + 3);// generate y
		floor = new char[x][y];// floor map contains panels and teleporter as P
								// and T
		char[][] object = new char[x][y];// object map contains rocks, obstacles
											// and r2d2 as R,O and 2
		for (int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++) {
				floor[i][j] = 'E';
				object[i][j] = 'E';
			}
		}

		int obstacles = (int) (Math.random() * 2 + 1);
		int rocks = (int) (Math.random() * 2 + 1);
		int px = (int) (Math.random() * x);// position for teleporter
		int py = (int) (Math.random() * y);
		floor[px][py] = 'T';
		for (int i = 0; i < obstacles; i++) {
			while (true) {
				px = (int) (Math.random() * x);// position for obstacle
				py = (int) (Math.random() * y);
				if (floor[px][py] == 'E') {// if position is empty
					object[px][py] = 'O';
					break;
				}
			}
		}
		for (int i = 0; i < rocks; i++) {
			while (true) {
				px = (int) (Math.random() * x);// position for panels
				py = (int) (Math.random() * y);
				if (floor[px][py] == 'E' && object[px][py] == 'E') {// if
																	// position
																	// is empty
					floor[px][py] = 'P';
					break;
				}
			}
		}
		for (int i = 0; i < rocks; i++) {
			while (true) {
				px = (int) (Math.random() * x);// position for rocks
				py = (int) (Math.random() * y);
				if (object[px][py] == 'E') {// if position is empty
					object[px][py] = 'R';
					break;
				}
			}
		}
		while (true) {
			px = (int) (Math.random() * x);// position for R2D2
			py = (int) (Math.random() * y);
			if (object[px][py] == 'E') {// if position is empty
				object[px][py] = '2';
				break;
			}
		}
		return object;
	}

	public ArrayList Search(char[][] grid, String strategy, boolean visualize) {
		Node root = GenerateTree(grid);
		if(strategy.equals("BF")) {
			breadthFirst(root);
		}
		else if(strategy.equals("DF")) {
			depthFirst(root);
		}
		else if(strategy.equals("ID")) {
			iterativeDeepening(root, 0);
		}
		else if(strategy.equals("UC")) {
			uniformCost(root);
		}
		else if(strategy.equals("GR1")) {
			GCost(root);
		}
		else if(strategy.equals("AS1")) {
			ACost(root);
		}
		else if(strategy.equals("GR2")) {
			GCost2(root);
		}
		else if(strategy.equals("AS2")) {
			ACost2(root);
		}
		
		else {
			System.out.println("Invalid strategy");
		}
		ArrayList<String> s = new ArrayList<String>();
		if (Goal != null) {
			Stack<Node> nodes = new Stack<Node>();
			Node temp = Goal;
			while (temp != null) {
				nodes.push(temp);
				temp = temp.Parent;
			}
			temp = nodes.pop();
			if (visualize) {
				for (int i = 0; i < temp.objectMap.length; i++) {
					System.out.println(temp.objectMap[i]);
				}
			}
			System.out.println();
			while (!nodes.isEmpty()) {
				temp = nodes.pop();
				if(temp == temp.Parent.getChildEast()) {
					s.add("left");
				}
				if(temp == temp.Parent.getChildWest()) {
					s.add("right");
				}
				if(temp == temp.Parent.getChildNorth()) {
					s.add("up");
				}
				if(temp == temp.Parent.getChildSouth()) {
					s.add("down");
				}
				if(visualize) {
					for (int i = 0; i < temp.objectMap.length; i++) {
						System.out.println(temp.objectMap[i]);
					}
					System.out.println();
				}
			}
		}
		ArrayList r = new ArrayList();
		r.add(s);
		if(Goal!=null) {
			r.add(Goal.AccumilatedCost);
		}
		else {
			r.add(0);
		}
		r.add(nodesSearched);
		return r;
	}

	public Node GenerateTree(char[][] grid) {
		Node root = new Node();
		root.setAccumilatedCost(0);
		root.objectMap = grid;
		root.setAEstimate(root.AEstimator(0, grid));
		root.setGEstimate(root.GEstimator(grid));
		root.AEstimate2=(root.AEstimator(0, grid));
		root.GEstimate2=(root.GEstimator(grid));
		root.setIsGoalState(root.checkGoal(floor));
		root.setDepth(0);
		root.setChildEast(MoveRight(root));
		root.setChildNorth(MoveUp(root));
		root.setChildSouth(MoveDown(root));
		root.setChildWest(MoveLeft(root));
		return root;
	}

	public Node MoveUp(Node parent) {
		Node n = new Node();
		n.setParent(parent);
		n.objectMap = new char[parent.objectMap.length][parent.objectMap[0].length];
		for (int i = 0; i < n.objectMap.length; i++) {
			for (int j = 0; j < n.objectMap[i].length; j++) {
				n.objectMap[i][j] = parent.objectMap[i][j];
			}
		}
		int costAdd = 1;
		for (int i = 0; i < parent.objectMap.length; i++) {
			for (int j = 0; j < parent.objectMap[i].length; j++) {
				if (parent.objectMap[i][j] == '2') {
					if (i != 0) {
						if (parent.objectMap[i - 1][j] == 'R') {
							if (i - 1 != 0) {
								if (parent.objectMap[i - 2][j] == 'E') {
									n.objectMap[i - 2][j] = 'R';
									n.objectMap[i - 1][j] = '2';
									n.objectMap[i][j] = 'E';
									costAdd = 2;
									break;
								}
							}
						}
						if (parent.objectMap[i - 1][j] == 'E') {
							n.objectMap[i - 1][j] = '2';
							n.objectMap[i][j] = 'E';
							break;
						}
					}
				}
			}
		}

		Node testNode = parent;
		while (testNode != null) {
			boolean equal = true;
			for (int i = 0; i < testNode.objectMap.length; i++) {
				for (int j = 0; j < testNode.objectMap[i].length; j++) {
					if (testNode.objectMap[i][j] != n.objectMap[i][j]) {
						equal = false;
						break;
					}
				}
			}
			if (equal) {
				return null;
			}
			testNode = testNode.Parent;
		}
		n.setDepth(parent.getDepth() + 1);
		n.setAccumilatedCost(parent.AccumilatedCost + costAdd);
		n.setAEstimate(n.AEstimator(n.AccumilatedCost, n.objectMap));
		n.setGEstimate(n.GEstimator(n.objectMap));
		n.AEstimate2=(n.AEstimator(n.AccumilatedCost, n.objectMap));
		n.GEstimate2=(n.GEstimator(n.objectMap));
		n.setIsGoalState(n.checkGoal(floor));
		if (n.isGoalState) {
			return n;
		}
		n.setChildEast(MoveRight(n));
		n.setChildNorth(MoveUp(n));
		n.setChildSouth(MoveDown(n));
		n.setChildWest(MoveLeft(n));
		return n;
	}

	public Node MoveDown(Node parent) {
		Node n = new Node();
		n.setParent(parent);
		n.objectMap = new char[parent.objectMap.length][parent.objectMap[0].length];
		for (int i = 0; i < n.objectMap.length; i++) {
			for (int j = 0; j < n.objectMap[i].length; j++) {
				n.objectMap[i][j] = parent.objectMap[i][j];
			}
		}
		int costAdd = 1;
		for (int i = 0; i < parent.objectMap.length; i++) {
			for (int j = 0; j < parent.objectMap[i].length; j++) {
				if (parent.objectMap[i][j] == '2') {
					if (i != parent.objectMap.length - 1) {
						if (parent.objectMap[i + 1][j] == 'R') {
							if (i + 1 != parent.objectMap.length - 1) {
								if (parent.objectMap[i + 2][j] == 'E') {
									n.objectMap[i + 2][j] = 'R';
									n.objectMap[i + 1][j] = '2';
									n.objectMap[i][j] = 'E';
									costAdd = 2;
									break;
								}
							}
						}
						if (parent.objectMap[i + 1][j] == 'E') {
							n.objectMap[i + 1][j] = '2';
							n.objectMap[i][j] = 'E';
							break;
						}
					}
				}
			}
		}
		Node testNode = parent;
		while (testNode != null) {
			boolean equal = true;
			for (int i = 0; i < testNode.objectMap.length; i++) {
				for (int j = 0; j < testNode.objectMap[i].length; j++) {
					if (testNode.objectMap[i][j] != n.objectMap[i][j]) {
						equal = false;
						break;
					}
				}
			}
			if (equal) {
				return null;
			}
			testNode = testNode.Parent;
		}
		n.setDepth(parent.getDepth() + 1);
		n.setAccumilatedCost(parent.AccumilatedCost + costAdd);
		n.setAEstimate(n.AEstimator(n.AccumilatedCost, n.objectMap));
		n.setGEstimate(n.GEstimator(n.objectMap));
		n.AEstimate2=(n.AEstimator(n.AccumilatedCost, n.objectMap));
		n.GEstimate2=(n.GEstimator(n.objectMap));
		n.setIsGoalState(n.checkGoal(floor));
		if (n.isGoalState) {
			return n;
		}
		n.setChildEast(MoveRight(n));
		n.setChildNorth(MoveUp(n));
		n.setChildSouth(MoveDown(n));
		n.setChildWest(MoveLeft(n));
		return n;
	}

	public Node MoveLeft(Node parent) {
		Node n = new Node();
		n.setParent(parent);
		n.objectMap = new char[parent.objectMap.length][parent.objectMap[0].length];
		for (int i = 0; i < n.objectMap.length; i++) {
			for (int j = 0; j < n.objectMap[i].length; j++) {
				n.objectMap[i][j] = parent.objectMap[i][j];
			}
		}
		int costAdd = 1;
		for (int i = 0; i < parent.objectMap.length; i++) {
			for (int j = 0; j < parent.objectMap[i].length; j++) {
				if (parent.objectMap[i][j] == '2') {
					if (j != parent.objectMap[i].length - 1) {
						if (parent.objectMap[i][j + 1] == 'R') {
							if (j + 1 != parent.objectMap[i].length - 1) {
								if (parent.objectMap[i][j + 2] == 'E') {
									n.objectMap[i][j + 2] = 'R';
									n.objectMap[i][j + 1] = '2';
									n.objectMap[i][j] = 'E';
									costAdd = 2;
									break;
								}
							}
						}
						if (parent.objectMap[i][j + 1] == 'E') {
							n.objectMap[i][j + 1] = '2';
							n.objectMap[i][j] = 'E';
							break;
						}
					}
				}
			}
		}
		Node testNode = parent;
		while (testNode != null) {
			boolean equal = true;
			for (int i = 0; i < testNode.objectMap.length; i++) {
				for (int j = 0; j < testNode.objectMap[i].length; j++) {
					if (testNode.objectMap[i][j] != n.objectMap[i][j]) {
						equal = false;
						break;
					}
				}
			}
			if (equal) {
				return null;
			}
			testNode = testNode.Parent;
		}
		n.setAccumilatedCost(parent.AccumilatedCost + costAdd);
		n.setAEstimate(n.AEstimator(n.AccumilatedCost, n.objectMap));
		n.setGEstimate(n.GEstimator(n.objectMap));
		n.AEstimate2=(n.AEstimator(n.AccumilatedCost, n.objectMap));
		n.GEstimate2=(n.GEstimator(n.objectMap));
		n.setIsGoalState(n.checkGoal(floor));
		if (n.isGoalState) {
			return n;
		}
		n.setChildEast(MoveRight(n));
		n.setChildNorth(MoveUp(n));
		n.setChildSouth(MoveDown(n));
		n.setChildWest(MoveLeft(n));
		return n;
	}

	public Node MoveRight(Node parent) {
		Node n = new Node();
		n.setParent(parent);
		n.objectMap = new char[parent.objectMap.length][parent.objectMap[0].length];
		for (int i = 0; i < n.objectMap.length; i++) {
			for (int j = 0; j < n.objectMap[i].length; j++) {
				n.objectMap[i][j] = parent.objectMap[i][j];
			}
		}
		int costAdd = 1;
		for (int i = 0; i < parent.objectMap.length; i++) {
			for (int j = 0; j < parent.objectMap[i].length; j++) {
				if (parent.objectMap[i][j] == '2') {
					if (j != 0) {
						if (parent.objectMap[i][j - 1] == 'R') {
							if (j - 1 != 0) {
								if (parent.objectMap[i][j - 2] == 'E') {
									n.objectMap[i][j - 2] = 'R';
									n.objectMap[i][j - 1] = '2';
									n.objectMap[i][j] = 'E';
									costAdd = 2;
									break;
								}
							}
						}
						if (parent.objectMap[i][j - 1] == 'E') {
							n.objectMap[i][j - 1] = '2';
							n.objectMap[i][j] = 'E';
							break;
						}
					}
				}
			}
		}
		Node testNode = parent;
		while (testNode != null) {
			boolean equal = true;
			for (int i = 0; i < testNode.objectMap.length; i++) {
				for (int j = 0; j < testNode.objectMap[i].length; j++) {
					if (testNode.objectMap[i][j] != n.objectMap[i][j]) {
						equal = false;
						break;
					}
				}
			}
			if (equal) {
				return null;
			}
			testNode = testNode.Parent;
		}
		n.setDepth(parent.getDepth() + 1);
		n.setAccumilatedCost(parent.AccumilatedCost + costAdd);
		n.setAEstimate(n.AEstimator(n.AccumilatedCost, n.objectMap));
		n.setGEstimate(n.GEstimator(n.objectMap));
		n.AEstimate2=(n.AEstimator(n.AccumilatedCost, n.objectMap));
		n.GEstimate2=(n.GEstimator(n.objectMap));
		n.setIsGoalState(n.checkGoal(floor));
		if (n.isGoalState) {
			return n;
		}
		n.setChildEast(MoveRight(n));
		n.setChildNorth(MoveUp(n));
		n.setChildSouth(MoveDown(n));
		n.setChildWest(MoveLeft(n));
		return n;
	}

	public void depthFirst(Node n) {
		if (n == null) {
			return;
		}
		nodesSearched++;
		if (n.isGoalState) {
			Goal = n;
		}
		if (Goal == null) {
			depthFirst(n.ChildNorth);
		}
		if (Goal == null) {
			depthFirst(n.ChildSouth);
		}
		if (Goal == null) {
			depthFirst(n.ChildEast);
		}
		if (Goal == null) {
			depthFirst(n.ChildWest);
		}
	}

	public void breadthFirst(Node n) {
		Queue<Node> q = new LinkedList<Node>();
		q.add(n);
		while (!q.isEmpty()) {
			Node temp = q.remove();
			nodesSearched++;
			if (temp.isGoalState) {
				Goal = temp;
				return;
			}
			if (temp.ChildEast != null) {
				q.add(temp.ChildEast);
			}
			if (temp.ChildNorth != null) {
				q.add(temp.ChildNorth);
			}
			if (temp.ChildWest != null) {
				q.add(temp.ChildWest);
			}
			if (temp.ChildSouth != null) {
				q.add(temp.ChildSouth);
			}
		}
	}

	public void iterativeDeepening(Node n, int depth) {
		Stack<Node> s = new Stack<Node>();
		s.push(n);
		boolean depthReached = false;
		while (!s.isEmpty()) {
			Node temp = s.pop();
			nodesSearched++;
			if (temp.isGoalState) {
				Goal = temp;
				return;
			}
			if (temp.Depth != depth) {
				if (temp.ChildEast != null) {
					s.push(temp.ChildEast);
				}
				if (temp.ChildNorth != null) {
					s.push(temp.ChildNorth);
				}
				if (temp.ChildWest != null) {
					s.push(temp.ChildWest);
				}
				if (temp.ChildSouth != null) {
					s.push(temp.ChildSouth);
				}
			} else {
				depthReached = true;
			}
		}
		if (depthReached) {
			iterativeDeepening(n, depth + 1);
		}
	}

	public void uniformCost(Node n) {
		Queue<Node> q = new LinkedList<Node>();
		q.add(n);
		while (!q.isEmpty()) {
			Node temp = q.remove();
			nodesSearched++;
			if (temp.isGoalState) {
				Goal = temp;
				return;
			}
			if (temp.ChildEast != null) {
				boolean found = false;
				for (int i = 0; i < q.size(); i++) {
					Node temp2 = q.remove();
					if (!found) {
						if (temp2.AccumilatedCost > temp.getChildEast().AccumilatedCost) {
							q.add(temp.ChildEast);
							q.add(temp2);
							i++;
							found = true;

						} else {
							q.add(temp2);
						}
					} else {
						q.add(temp2);
					}
				}
				if (!found) {
					q.add(temp.ChildEast);
				}
			}
			if (temp.ChildNorth != null) {
				boolean found = false;
				for (int i = 0; i < q.size(); i++) {
					Node temp2 = q.remove();
					if (!found) {
						if (temp2.AccumilatedCost > temp.getChildNorth().AccumilatedCost) {
							q.add(temp.ChildNorth);
							q.add(temp2);
							i++;
							found = true;

						} else {
							q.add(temp2);
						}
					} else {
						q.add(temp2);
					}
				}
				if (!found) {
					q.add(temp.ChildNorth);
				}
			}
			if (temp.ChildWest != null) {
				boolean found = false;
				for (int i = 0; i < q.size(); i++) {
					Node temp2 = q.remove();
					if (!found) {
						if (temp2.AccumilatedCost > temp.getChildWest().AccumilatedCost) {
							q.add(temp.ChildWest);
							q.add(temp2);
							i++;
							found = true;

						} else {
							q.add(temp2);
						}
					} else {
						q.add(temp2);
					}
				}
				if (!found) {
					q.add(temp.ChildWest);
				}
			}
			if (temp.ChildSouth != null) {
				boolean found = false;
				for (int i = 0; i < q.size(); i++) {
					Node temp2 = q.remove();
					if (!found) {
						if (temp2.AccumilatedCost > temp.getChildSouth().AccumilatedCost) {
							q.add(temp.ChildSouth);
							q.add(temp2);
							i++;
							found = true;

						} else {
							q.add(temp2);
						}
					} else {
						q.add(temp2);
					}
				}
				if (!found) {
					q.add(temp.ChildSouth);
				}
			}
		}
	}

	public void ACost(Node n) {
		Queue<Node> q = new LinkedList<Node>();
		q.add(n);
		while (!q.isEmpty()) {
			Node temp = q.remove();
			nodesSearched++;
			if (temp.isGoalState) {
				Goal = temp;
				return;
			}
			if (temp.ChildEast != null) {
				boolean found = false;
				for (int i = 0; i < q.size(); i++) {
					Node temp2 = q.remove();
					if (!found) {
						if (temp2.AEstimate > temp.getChildEast().AEstimate) {
							q.add(temp.ChildEast);
							q.add(temp2);
							i++;
							found = true;

						} else {
							q.add(temp2);
						}
					} else {
						q.add(temp2);
					}
				}
				if (!found) {
					q.add(temp.ChildEast);
				}
			}
			if (temp.ChildNorth != null) {
				boolean found = false;
				for (int i = 0; i < q.size(); i++) {
					Node temp2 = q.remove();
					if (!found) {
						if (temp2.AEstimate > temp.getChildNorth().AEstimate) {
							q.add(temp.ChildNorth);
							q.add(temp2);
							i++;
							found = true;

						} else {
							q.add(temp2);
						}
					} else {
						q.add(temp2);
					}
				}
				if (!found) {
					q.add(temp.ChildNorth);
				}
			}
			if (temp.ChildWest != null) {
				boolean found = false;
				for (int i = 0; i < q.size(); i++) {
					Node temp2 = q.remove();
					if (!found) {
						if (temp2.AEstimate > temp.getChildWest().AEstimate) {
							q.add(temp.ChildWest);
							q.add(temp2);
							i++;
							found = true;

						} else {
							q.add(temp2);
						}
					} else {
						q.add(temp2);
					}
				}
				if (!found) {
					q.add(temp.ChildWest);
				}
			}
			if (temp.ChildSouth != null) {
				boolean found = false;
				for (int i = 0; i < q.size(); i++) {
					Node temp2 = q.remove();
					if (!found) {
						if (temp2.AEstimate > temp.getChildSouth().AEstimate) {
							q.add(temp.ChildSouth);
							q.add(temp2);
							i++;
							found = true;

						} else {
							q.add(temp2);
						}
					} else {
						q.add(temp2);
					}
				}
				if (!found) {
					q.add(temp.ChildSouth);
				}
			}
		}
	}

	public void GCost(Node n) {
		Queue<Node> q = new LinkedList<Node>();
		q.add(n);
		while (!q.isEmpty()) {
			Node temp = q.remove();
			nodesSearched++;
			if (temp.isGoalState) {
				Goal = temp;
				return;
			}
			if (temp.ChildEast != null) {
				boolean found = false;
				for (int i = 0; i < q.size(); i++) {
					Node temp2 = q.remove();
					if (!found) {
						if (temp2.GEstimate > temp.getChildEast().GEstimate) {
							q.add(temp.ChildEast);
							q.add(temp2);
							i++;
							found = true;

						} else {
							q.add(temp2);
						}
					} else {
						q.add(temp2);
					}
				}
				if (!found) {
					q.add(temp.ChildEast);
				}
			}
			if (temp.ChildNorth != null) {
				boolean found = false;
				for (int i = 0; i < q.size(); i++) {
					Node temp2 = q.remove();
					if (!found) {
						if (temp2.GEstimate > temp.getChildNorth().GEstimate) {
							q.add(temp.ChildNorth);
							q.add(temp2);
							i++;
							found = true;

						} else {
							q.add(temp2);
						}
					} else {
						q.add(temp2);
					}
				}
				if (!found) {
					q.add(temp.ChildNorth);
				}
			}
			if (temp.ChildWest != null) {
				boolean found = false;
				for (int i = 0; i < q.size(); i++) {
					Node temp2 = q.remove();
					if (!found) {
						if (temp2.GEstimate > temp.getChildWest().GEstimate) {
							q.add(temp.ChildWest);
							q.add(temp2);
							i++;
							found = true;

						} else {
							q.add(temp2);
						}
					} else {
						q.add(temp2);
					}
				}
				if (!found) {
					q.add(temp.ChildWest);
				}
			}
			if (temp.ChildSouth != null) {
				boolean found = false;
				for (int i = 0; i < q.size(); i++) {
					Node temp2 = q.remove();
					if (!found) {
						if (temp2.GEstimate > temp.getChildSouth().GEstimate) {
							q.add(temp.ChildSouth);
							q.add(temp2);
							i++;
							found = true;

						} else {
							q.add(temp2);
						}
					} else {
						q.add(temp2);
					}
				}
				if (!found) {
					q.add(temp.ChildSouth);
				}
			}
		}
	}
	public void ACost2(Node n) {
		Queue<Node> q = new LinkedList<Node>();
		q.add(n);
		while (!q.isEmpty()) {
			Node temp = q.remove();
			nodesSearched++;
			if (temp.isGoalState) {
				Goal = temp;
				return;
			}
			if (temp.ChildEast != null) {
				boolean found = false;
				for (int i = 0; i < q.size(); i++) {
					Node temp2 = q.remove();
					if (!found) {
						if (temp2.AEstimate2 > temp.getChildEast().AEstimate2) {
							q.add(temp.ChildEast);
							q.add(temp2);
							i++;
							found = true;

						} else {
							q.add(temp2);
						}
					} else {
						q.add(temp2);
					}
				}
				if (!found) {
					q.add(temp.ChildEast);
				}
			}
			if (temp.ChildNorth != null) {
				boolean found = false;
				for (int i = 0; i < q.size(); i++) {
					Node temp2 = q.remove();
					if (!found) {
						if (temp2.AEstimate2 > temp.getChildNorth().AEstimate2) {
							q.add(temp.ChildNorth);
							q.add(temp2);
							i++;
							found = true;

						} else {
							q.add(temp2);
						}
					} else {
						q.add(temp2);
					}
				}
				if (!found) {
					q.add(temp.ChildNorth);
				}
			}
			if (temp.ChildWest != null) {
				boolean found = false;
				for (int i = 0; i < q.size(); i++) {
					Node temp2 = q.remove();
					if (!found) {
						if (temp2.AEstimate2 > temp.getChildWest().AEstimate2) {
							q.add(temp.ChildWest);
							q.add(temp2);
							i++;
							found = true;

						} else {
							q.add(temp2);
						}
					} else {
						q.add(temp2);
					}
				}
				if (!found) {
					q.add(temp.ChildWest);
				}
			}
			if (temp.ChildSouth != null) {
				boolean found = false;
				for (int i = 0; i < q.size(); i++) {
					Node temp2 = q.remove();
					if (!found) {
						if (temp2.AEstimate2 > temp.getChildSouth().AEstimate2) {
							q.add(temp.ChildSouth);
							q.add(temp2);
							i++;
							found = true;

						} else {
							q.add(temp2);
						}
					} else {
						q.add(temp2);
					}
				}
				if (!found) {
					q.add(temp.ChildSouth);
				}
			}
		}
	}

	public void GCost2(Node n) {
		Queue<Node> q = new LinkedList<Node>();
		q.add(n);
		while (!q.isEmpty()) {
			Node temp = q.remove();
			nodesSearched++;
			if (temp.isGoalState) {
				Goal = temp;
				return;
			}
			if (temp.ChildEast != null) {
				boolean found = false;
				for (int i = 0; i < q.size(); i++) {
					Node temp2 = q.remove();
					if (!found) {
						if (temp2.GEstimate2 > temp.getChildEast().GEstimate2) {
							q.add(temp.ChildEast);
							q.add(temp2);
							i++;
							found = true;

						} else {
							q.add(temp2);
						}
					} else {
						q.add(temp2);
					}
				}
				if (!found) {
					q.add(temp.ChildEast);
				}
			}
			if (temp.ChildNorth != null) {
				boolean found = false;
				for (int i = 0; i < q.size(); i++) {
					Node temp2 = q.remove();
					if (!found) {
						if (temp2.GEstimate2 > temp.getChildNorth().GEstimate2) {
							q.add(temp.ChildNorth);
							q.add(temp2);
							i++;
							found = true;

						} else {
							q.add(temp2);
						}
					} else {
						q.add(temp2);
					}
				}
				if (!found) {
					q.add(temp.ChildNorth);
				}
			}
			if (temp.ChildWest != null) {
				boolean found = false;
				for (int i = 0; i < q.size(); i++) {
					Node temp2 = q.remove();
					if (!found) {
						if (temp2.GEstimate2 > temp.getChildWest().GEstimate2) {
							q.add(temp.ChildWest);
							q.add(temp2);
							i++;
							found = true;

						} else {
							q.add(temp2);
						}
					} else {
						q.add(temp2);
					}
				}
				if (!found) {
					q.add(temp.ChildWest);
				}
			}
			if (temp.ChildSouth != null) {
				boolean found = false;
				for (int i = 0; i < q.size(); i++) {
					Node temp2 = q.remove();
					if (!found) {
						if (temp2.GEstimate2 > temp.getChildSouth().GEstimate2) {
							q.add(temp.ChildSouth);
							q.add(temp2);
							i++;
							found = true;

						} else {
							q.add(temp2);
						}
					} else {
						q.add(temp2);
					}
				}
				if (!found) {
					q.add(temp.ChildSouth);
				}
			}
		}
	}

	public static void main(String[] args) {
		R2D2 r2d2 = new R2D2();
		char[][] map = r2d2.generateMap();
		for (int i = 0; i < r2d2.floor.length; i++) {
			System.out.println(r2d2.floor[i]);
		}
		System.out.println();
		for (int i = 0; i < map.length; i++) {
			System.out.println(map[i]);
		}
		System.out.println();
		ArrayList a = r2d2.Search(map, "BF", true);
		System.out.println(a.get(0));
		System.out.println(a.get(1));
		System.out.println(a.get(2));
	}
}
