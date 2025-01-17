import javax.swing.*;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;

public class Flow {
	static long startTime = 0;
	static int frameX;
	static int frameY;
	static FlowPanel fp;
	static Parallelize[] waterThreads = new Parallelize[4];//subject to name change


	// start timer
	private static void tick(){
		startTime = System.currentTimeMillis();
	}
	
	// stop timer, return time elapsed in seconds
	private static float tock(){
		return (System.currentTimeMillis() - startTime) / 1000.0f; 
	}
	
	public static void setupGUI(int frameX,int frameY,Terrain landdata) {
		
	Dimension fsize = new Dimension(800, 800);
    	JFrame frame = new JFrame("Waterflow"); 
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.getContentPane().setLayout(new BorderLayout());
	//frame.getContentPane().addMouseListener(new WaterClickListener());
    	
      	JPanel g = new JPanel();
        g.setLayout(new BoxLayout(g, BoxLayout.PAGE_AXIS)); 
   
		fp = new FlowPanel(landdata);
		waterThreads[0] = new Parallelize(0,landdata.dim());
		//waterThreads[1] = new Parallelize(landdata.dim()/4,landdata.dim()/2);
		//waterThreads[2] = new Parallelize(landdata.dim()/2,3*(landdata.dim())/4);
		//waterThreads[3] = new Parallelize(3*(landdata.dim())/4,landdata.dim());

		fp.setPreferredSize(new Dimension(frameX,frameY));
		fp.addMouseListener(new WaterClickListener());
		//Thread fpt = new Thread(fp);
		g.add(fp);
		//g.add(fp2);
		//g.addMouseListener(new WaterClickListener());
	    
		// to do: add a MouseListener, buttons and ActionListeners on those buttons
	   	//frame.getContentPane().addMouseListener(new WaterClickListener());

		JPanel b = new JPanel();
		b.setLayout(new BoxLayout(b, BoxLayout.LINE_AXIS));
		JButton resetB = new JButton("Reset");
		resetB.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				fp.clearScreen();
			}
		});	
		
		JButton pauseB = new JButton("Pause");
		pauseB.addActionListener(new ActionListener(){
			public void actionPerformed( ActionEvent e){ 
				for(int i = 0; i < 1; i++){
					waterThreads[i].pause();
				}
			}
		});

		JButton playB = new JButton("Play");
		playB.addActionListener(new ActionListener(){
			public void actionPerformed( ActionEvent e){
				for(int i = 0; i < 1; i++){
					waterThreads[i].play();
				}
			}
		});

		JButton endB = new JButton("End");;
		// add the listener to the jbutton to handle the "pressed" event
		endB.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// to do ask threads to stop
				frame.dispose();
				for(int i = 0; i < 1; i++){
					waterThreads[i].loop = false;
				}
			}
		});

		b.add(resetB);
		b.add(Box.createRigidArea(new Dimension(50,0)));
		b.add(pauseB);
		b.add(Box.createRigidArea(new Dimension(50,0)));
		b.add(playB);
		b.add(Box.createRigidArea(new Dimension(50,0)));
		b.add(endB);
		g.add(b);
		//fng.addMouseListener(new WaterClickListener());

    	
	frame.setSize(frameX, frameY+50);	// a little extra space at the bottom for buttons
      	frame.setLocationRelativeTo(null);  // center window on screen
      	frame.add(g); //add contents to window
	frame.setContentPane(g);
	//frame.getContentPane().addMouseListener(new WaterClickListener());
        frame.setVisible(true);
        Thread fpt = new Thread(fp);
        fpt.start();
	//Thread fpt2 = new Thread(fp2);
	//fpt2.start();
	Thread [] para = new Thread[4];
	for(int i = 0; i < 1; i++){
		para[i] = new Thread(waterThreads[i]);
	}
	for(int j = 0; j < 1; j++){
		para[j].start();
	}

	}
	
		
	public static void main(String[] args) {
		Terrain landdata = new Terrain();
		
		// check that number of command line arguments is correct
		if(args.length != 1)
		{
			System.out.println("Incorrect number of command line arguments. Should have form: java -jar flow.java intputfilename");
			System.exit(0);
		}
				
		// landscape information from file supplied as argument
		// 
		landdata.readData(args[0]);
		frameX = landdata.getDimX();
		frameY = landdata.getDimY();
		SwingUtilities.invokeLater(()->setupGUI(frameX, frameY, landdata));

		// to do: initialise and start simulation
		/*Prallelize para = new Parallelize(5,10);
		Thread paraThread = new Thread(para);*/
		/*Thread [] para = new Thread[5];
		for(int i = 0; i < 1; i++)
		{
			para[i] = new Thread(new Parallelize(i, i+8));
		}
		para[0].start();*/
		/*para[1].start();
		para[2].start();
		para[3].start();
		para[4].start();*/
		//System.out.println(fp.land.dim());





		//paraThread.start();
	}
}
