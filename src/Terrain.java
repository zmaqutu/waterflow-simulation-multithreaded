import java.io.File;
import java.awt.image.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.PrintWriter;

public class Terrain {

	volatile static Points grid[][]; // regular grid of height values
	int dimx, dimy; // data dimensions
	static BufferedImage img; // greyscale image for displaying the terrain top-down
	static BufferedImage waterImage;

	volatile ArrayList<Integer> permute;	// permuted list of integers in range [0, dimx*dimy)
	
	// overall number of elements in the height grid
	int dim(){
		return dimx*dimy;
	}
	// get the array of heights
	static Points[][] getGrid(){
		return grid;
	}
	static BufferedImage getWaterImage(){
		return waterImage;
	}
	
	// get x-dimensions (number of columns)
	int getDimX(){
		return dimx;
	}
	
	// get y-dimensions (number of rows)
	int getDimY(){
		return dimy;
	}
	
	// get greyscale image
	static BufferedImage getImage() {
		  return img;
	}
	
	// convert linear position into 2D location in grid
	int [] locate(int position, int [] ind)
	{
		ind[0] = (int) position / dimy; // x
		ind[1] = position % dimy; // y
		
		return ind;	// return x y coordinates of pos
	}
	
	// convert height values to greyscale colour and populate an image
	void deriveImage()
	{
		img = new BufferedImage(dimx, dimy, BufferedImage.TYPE_INT_ARGB);
		waterImage = new BufferedImage(dimx, dimy, BufferedImage.TYPE_INT_ARGB);
		float maxh = -10000.0f, minh = 10000.0f;
		
		// determine range of heights
		for(int x=0; x < dimx; x++)
			for(int y=0; y < dimy; y++) {
				float h = grid[x][y].getHeight();
				if(h > maxh)
					maxh = h;
				if(h < minh)
					minh = h;
			}
		
		for(int x=0; x < dimx; x++)
			for(int y=0; y < dimy; y++) {
				 // find normalized height value in range
				 float val = (grid[x][y].getHeight() - minh) / (maxh - minh);
				 Color col = new Color(val, val, val, 1.0f);
				 Color foregroundColor = new Color(106,195,255, 0);
				 img.setRGB(x, y, col.getRGB());
				 waterImage.setRGB(x,y,foregroundColor.getRGB());
			}
	}
	
	// generate a permuted list of linear index positions to allow a random
	// traversal over the terrain
	void genPermute() {
		permute = new ArrayList<Integer>();
		for(int idx = 0; idx < dim(); idx++)
			permute.add(idx);
		java.util.Collections.shuffle(permute);
	}
	
	// find permuted 2D location from a linear index in the
	// range [0, dimx*dimy)
	int [] getPermute(int i, int [] loc) {
		int[] location = locate(permute.get(i), loc);
		return location;
	}
	
	// read in terrain from file
	void readData(String fileName){ 
		try{ 
			Scanner sc = new Scanner(new File(fileName));
			
			// read grid dimensions
			// x and y correpond to columns and rows, respectively.
			// Using image coordinate system where top left is (0, 0).
			dimy = sc.nextInt(); 
			dimx = sc.nextInt();
			
			// populate height grid
			//height = new float[dimy][dimx];
			grid = new Points[dimx][dimy];
			
			for(int y = 0; y < dimy; y++){
				for(int x = 0; x < dimx; x++)	
					grid[x][y] = new Points(sc.nextFloat(), 0, 0);//an array of objects with a height & (wD and wS) = 0
				}
				
			sc.close(); 
			
			// create randomly permuted list of indices for traversal 
			genPermute(); 
			
			// generate greyscale heightfield image
			deriveImage();
		} 
		catch (IOException e){ 
			System.out.println("Unable to open input file "+fileName);
			e.printStackTrace();
		}
		catch (java.util.InputMismatchException e){ 
			System.out.println("Malformed input file "+fileName);
			e.printStackTrace();
		}
	}
}
