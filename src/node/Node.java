package node;

import java.util.ArrayList;

public class Node {
	public Node ChildNorth;
	public Node ChildSouth;
	public Node ChildEast;
	public Node ChildWest;
	public Node Parent;
	public char[][] objectMap;
	Boolean isGoalState;
	public int AEstimate;
	public int GEstimate;
	public int AEstimate2;
	public int GEstimate2;
	public int AccumilatedCost;
	public int Depth;
	
	public Node() {
		Parent = null;
		ChildNorth = null;
		ChildSouth = null;
		ChildEast = null;
		ChildWest = null;
		isGoalState = false;
		AEstimate = 0;
		GEstimate = 0;
		AccumilatedCost =0;
		Depth = 0;
	}

	public Node getChildNorth() {
		return ChildNorth;
	}


	public void setChildNorth(Node childNorth) {
		ChildNorth = childNorth;
	}


	public Node getChildSouth() {
		return ChildSouth;
	}


	public void setChildSouth(Node childSouth) {
		ChildSouth = childSouth;
	}


	public Node getChildEast() {
		return ChildEast;
	}


	public void setChildEast(Node childEast) {
		ChildEast = childEast;
	}


	public Node getChildWest() {
		return ChildWest;
	}


	public void setChildWest(Node childWest) {
		ChildWest = childWest;
	}


	public Node getParent() {
		return Parent;
	}


	public void setParent(Node parent) {
		Parent = parent;
	}



	public Boolean getIsGoalState() {
		return isGoalState;
	}


	public void setIsGoalState(Boolean isGoalState) {
		this.isGoalState = isGoalState;
	}


	public int getAEstimate() {
		return AEstimate;
	}


	public void setAEstimate(int aEstimate) {
		AEstimate = aEstimate;
	}


	public int getGEstimate() {
		return GEstimate;
	}


	public void setGEstimate(int gEstimate) {
		GEstimate = gEstimate;
	}


	public int getAccumilatedCost() {
		return AccumilatedCost;
	}


	public void setAccumilatedCost(int accumilatedCost) {
		AccumilatedCost = accumilatedCost;
	}


	public int getDepth() {
		return Depth;
	}


	public void setDepth(int depth) {
		Depth = depth;
	}
	public int AEstimator(int cost, char[][] grid){
		ArrayList<int[]> rocks = new ArrayList<int[]>();
		ArrayList<int[]> panels = new ArrayList<int[]>();
		int[] r2d2pos = new int[2];
		int[] telepos = new int[2];
		for(int i = 0; i< grid.length;i++){
			for(int j = 0; j<grid[i].length;j++){
				if(grid[i][j] == 'T'){
					telepos[0] = i;
					telepos[1] = j;
				}
				if(grid[i][j] == 'P'){
					int[] panelpos = {i,j};
					panels.add(panelpos);
				}
				if(objectMap[i][j] == '2'){
					r2d2pos[0] = i;
					r2d2pos[1] = j;
				}
				if(objectMap[i][j] == 'R'){
					int[] rockpos = {i,j};
					panels.add(rockpos);
				}
			}
		}
		int rockestimate = 0;
		for(int i = 0; i<rocks.size();i++){
			int mindiff = Integer.MAX_VALUE;
			for(int j = 0; j<panels.size();j++){
				int xdiff = Math.abs(rocks.get(i)[0] - panels.get(j)[0]);
				int ydiff = Math.abs(rocks.get(i)[1] - panels.get(j)[1]);
				int totaldiff = xdiff + ydiff;
				if(totaldiff<mindiff){
					mindiff = totaldiff;
				}
			}
			rockestimate +=mindiff;
			}
		rockestimate *=2;
		
		int teleportestimate = Math.abs(telepos[0] - r2d2pos[0]) + Math.abs(telepos[1] - r2d2pos[1]);
		
		return rockestimate + teleportestimate + cost;
	}
	public int GEstimator(char[][] grid){
		ArrayList<int[]> rocks = new ArrayList<int[]>();
		ArrayList<int[]> panels = new ArrayList<int[]>();
		int[] r2d2pos = new int[2];
		int[] telepos = new int[2];
		for(int i = 0; i< grid.length;i++){
			for(int j = 0; j<grid[i].length;j++){
				if(grid[i][j] == 'T'){
					telepos[0] = i;
					telepos[1] = j;
				}
				if(grid[i][j] == 'P'){
					int[] panelpos = {i,j};
					panels.add(panelpos);
				}
				if(objectMap[i][j] == '2'){
					r2d2pos[0] = i;
					r2d2pos[1] = j;
				}
				if(objectMap[i][j] == 'R'){
					int[] rockpos = {i,j};
					panels.add(rockpos);
				}
			}
		}
		int rockestimate = 0;
		for(int i = 0; i<rocks.size();i++){
			int mindiff = Integer.MAX_VALUE;
			for(int j = 0; j<panels.size();j++){
				int xdiff = Math.abs(rocks.get(i)[0] - panels.get(j)[0]);
				int ydiff = Math.abs(rocks.get(i)[1] - panels.get(j)[1]);
				int totaldiff = xdiff + ydiff;
				if(totaldiff<mindiff){
					mindiff = totaldiff;
				}
			}
			rockestimate +=mindiff;
			}
		rockestimate *=2;
		
		int teleportestimate = Math.abs(telepos[0] - r2d2pos[0]) + Math.abs(telepos[1] - r2d2pos[1]);
		
		return rockestimate + teleportestimate;
	}
	public int AEstimator2(int cost, char[][] grid){
		ArrayList<int[]> rocks = new ArrayList<int[]>();
		ArrayList<int[]> panels = new ArrayList<int[]>();
		int[] r2d2pos = new int[2];
		int[] telepos = new int[2];
		for(int i = 0; i< grid.length;i++){
			for(int j = 0; j<grid[i].length;j++){
				if(grid[i][j] == 'T'){
					telepos[0] = i;
					telepos[1] = j;
				}
				if(grid[i][j] == 'P'){
					int[] panelpos = {i,j};
					panels.add(panelpos);
				}
				if(objectMap[i][j] == '2'){
					r2d2pos[0] = i;
					r2d2pos[1] = j;
				}
				if(objectMap[i][j] == 'R'){
					int[] rockpos = {i,j};
					panels.add(rockpos);
				}
			}
		}
		int rockestimate = 0;
		for(int i = 0; i<rocks.size();i++){
			int mindiff = Integer.MAX_VALUE;
			for(int j = 0; j<panels.size();j++){
				int xdiff = Math.abs(rocks.get(i)[0] - panels.get(j)[0]);
				int ydiff = Math.abs(rocks.get(i)[1] - panels.get(j)[1]);
				int totaldiff = xdiff + ydiff;
				if(totaldiff<mindiff){
					mindiff = totaldiff;
				}
			}
			rockestimate +=mindiff;
			}
		rockestimate *=3;
		
		int teleportestimate = Math.abs(telepos[0] - r2d2pos[0]) + Math.abs(telepos[1] - r2d2pos[1]);
		
		return rockestimate + teleportestimate + cost;
	}
	public int GEstimator2(char[][] grid){
		ArrayList<int[]> rocks = new ArrayList<int[]>();
		ArrayList<int[]> panels = new ArrayList<int[]>();
		int[] r2d2pos = new int[2];
		int[] telepos = new int[2];
		for(int i = 0; i< grid.length;i++){
			for(int j = 0; j<grid[i].length;j++){
				if(grid[i][j] == 'T'){
					telepos[0] = i;
					telepos[1] = j;
				}
				if(grid[i][j] == 'P'){
					int[] panelpos = {i,j};
					panels.add(panelpos);
				}
				if(objectMap[i][j] == '2'){
					r2d2pos[0] = i;
					r2d2pos[1] = j;
				}
				if(objectMap[i][j] == 'R'){
					int[] rockpos = {i,j};
					panels.add(rockpos);
				}
			}
		}
		int rockestimate = 0;
		for(int i = 0; i<rocks.size();i++){
			int mindiff = Integer.MAX_VALUE;
			for(int j = 0; j<panels.size();j++){
				int xdiff = Math.abs(rocks.get(i)[0] - panels.get(j)[0]);
				int ydiff = Math.abs(rocks.get(i)[1] - panels.get(j)[1]);
				int totaldiff = xdiff + ydiff;
				if(totaldiff<mindiff){
					mindiff = totaldiff;
				}
			}
			rockestimate +=mindiff;
			}
		rockestimate *=3;
		
		int teleportestimate = Math.abs(telepos[0] - r2d2pos[0]) + Math.abs(telepos[1] - r2d2pos[1]);
		
		return rockestimate + teleportestimate;
	}
	public boolean checkGoal(char[][] floorGrid){
		for(int i = 0; i< floorGrid.length;i++){
			for(int j = 0; j<floorGrid[i].length;j++){
				if(floorGrid[i][j] == 'P'){
					if(objectMap[i][j]!='R'){
						return false;
					}
				}
				if(floorGrid[i][j] == 'T'){
					if(objectMap[i][j]!='2'){
						return false;
					}
				}
			}
		}
		
		return true;
	}
}
