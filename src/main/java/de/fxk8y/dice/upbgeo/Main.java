package de.fxk8y.dice.upbgeo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import crosby.binary.osmosis.OsmosisReader;


public class Main {
	
	
	public static void main(String[] args) {
		System.out.println("Hallo, UPB!");

		InputStream inputStream;
		try {
			inputStream = new FileInputStream("nordrhein-westfalen-latest.osm.pbf");
	        OsmosisReader reader = new OsmosisReader(inputStream);	        
	        reader.setSink(new OSMSink());
	        reader.run();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    }
}
