package com.remnant.map;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

import com.remnant.graphics.Light;
import com.remnant.ui.EditorProperties;

public class MapIO {
	
	static final String MAP_RESOURCE_PATH = "resources/maps";
	static final String MAP_RESOURCE_EXT = "map";
	
	static final String NULL_CHUNK_ID = "NULL";
	static final String MAP_PROPERTIES_CHUNK_ID = "MAP-PROPERTIES";
	static final String EDITOR_PROPERTIES_CHUNK_ID = "EDITOR-PROPERTIES";
	static final String TERRAIN_DATA_CHUNK_ID = "TERRAIN-DATA";
	static final String LIGHT_DATA_CHUNK_ID = "LIGHT-DATA";
	static final String COLLIDER_DATA_CHUNK_ID = "COLLIDER-DATA";
	static final String PICKUP_DATA_CHUNK_ID = "PICKUP-DATA";
	
	static String chunkType = NULL_CHUNK_ID;
	
	static Vector2f creationPosition = new Vector2f(0, 0);
	static TerrainDefinition terrainProperties = new TerrainDefinition();
	static LightDefinition lightProperties = new LightDefinition();
	static ColliderDefinition colliderProperties =  new ColliderDefinition();
	static PickupDefinition pickupProperties = new PickupDefinition();
	
	public static void read(String mapName, Map fileToLoad, EditorProperties properties){
		Path mapPath = Paths.get(MAP_RESOURCE_PATH, mapName);
		try {
			List<String> lines = Files.readAllLines(mapPath, Charset.defaultCharset());
			System.out.println("[MapIO]["+mapName+"] Map File Read.");
			for(int i = 0; i < lines.size(); i++){
				String line = lines.get(i);
				
				String parsedChunkID = NULL_CHUNK_ID;
				if(line.startsWith("CHUNK"))
					parsedChunkID = line.split("=")[1].trim();
				
				
				if(parsedChunkID.equals(MAP_PROPERTIES_CHUNK_ID))
					chunkType = MAP_PROPERTIES_CHUNK_ID;
				else if(parsedChunkID.equals(EDITOR_PROPERTIES_CHUNK_ID))
					chunkType = EDITOR_PROPERTIES_CHUNK_ID;
				else if(parsedChunkID.equals(TERRAIN_DATA_CHUNK_ID))
					chunkType = TERRAIN_DATA_CHUNK_ID;
				else if(parsedChunkID.equals(LIGHT_DATA_CHUNK_ID))
					chunkType = LIGHT_DATA_CHUNK_ID;
				else if(parsedChunkID.equals(COLLIDER_DATA_CHUNK_ID))
					chunkType = COLLIDER_DATA_CHUNK_ID;
				else if(parsedChunkID.equals(PICKUP_DATA_CHUNK_ID))
					chunkType = PICKUP_DATA_CHUNK_ID;
				
				line = line.trim();
				if(chunkType.equals(MAP_PROPERTIES_CHUNK_ID)){
					//If line is not a chunk ID
					if(parsedChunkID.equals(NULL_CHUNK_ID)){
						if(line.startsWith("name")){
							String name = line.split("=")[1].trim();
							fileToLoad.name = name;
						}
					}
				} else if(chunkType.equals(EDITOR_PROPERTIES_CHUNK_ID)){
					//If line is not a chunk ID
					if(parsedChunkID.equals(NULL_CHUNK_ID)){
						if(line.startsWith("cameraX")){
							String token = line.split("=")[1].trim();
							properties.cameraPosition.x = Float.parseFloat(token);
						} else if(line.startsWith("cameraY")){
							String token = line.split("=")[1].trim();
							properties.cameraPosition.y = Float.parseFloat(token);
						} else if(line.startsWith("cameraZoomX")){
							String token = line.split("=")[1].trim();
							properties.cameraZoom.x = Float.parseFloat(token);
						} else if(line.startsWith("cameraZoomY")){
							String token = line.split("=")[1].trim();
							properties.cameraZoom.y = Float.parseFloat(token);
						} else if(line.startsWith("numDivisions")){
							String token = line.split("=")[1].trim();
							properties.numDivisions = Integer.parseInt(token);
						} else if(line.startsWith("cellSize")){
							String token = line.split("=")[1].trim();
							properties.cellSize = Integer.parseInt(token);
						} else if(line.startsWith("snapToGrid")){
							String token = line.split("=")[1].trim();
							properties.snapToGrid = Boolean.parseBoolean(token);
						} else if(line.startsWith("drawGrid")){
							String token = line.split("=")[1].trim();
							properties.drawGrid = Boolean.parseBoolean(token);
						}
					}
				} else if(chunkType.equals(TERRAIN_DATA_CHUNK_ID)){
					if(parsedChunkID.equals(NULL_CHUNK_ID)){
						String[] tokens = line.split(",");
						for(int j = 0; j < tokens.length; j++){
							String token = tokens[j].trim();
							String name = token.split("=")[0].trim();
							String value = token.split("=")[1].trim();
							if(name.equals("x")){
								creationPosition.x = Float.parseFloat(value);
							} else if(name.equals("y")){
								creationPosition.y = Float.parseFloat(value);
							} else if(name.equals("orientation")){
								terrainProperties.orientation = Float.parseFloat(value);
							} else if(name.equals("path")){
								terrainProperties.diffusePath = value;
							} else if(name.equals("normalPath")){
								terrainProperties.normalPath = value;
							} else if(name.equals("emissivePath")){
								terrainProperties.emissivePath = value;
							} else if(name.equals("size")){
								terrainProperties.size = Integer.parseInt(value);
							} else if(name.equals("type")){
								terrainProperties.type = value;
							}
						}
						fileToLoad.createTerrainAt(creationPosition.x, creationPosition.y, terrainProperties);
					}
				} else if(chunkType.equals(LIGHT_DATA_CHUNK_ID)){
					if(parsedChunkID.equals(NULL_CHUNK_ID)){
						String[] tokens = line.split(",");
						for(int j = 0; j < tokens.length; j++){
							String token = tokens[j].trim();
							String name = token.split("=")[0].trim();
							String value = token.split("=")[1].trim();
							if(name.equals("x")){
								creationPosition.x = Float.parseFloat(value);
							} else if(name.equals("y")){
								creationPosition.y = Float.parseFloat(value);
							} else if(name.equals("r")){
								lightProperties.color.x = Float.parseFloat(value);
							} else if(name.equals("g")){
								lightProperties.color.y = Float.parseFloat(value);
							} else if(name.equals("b")){
								lightProperties.color.z = Float.parseFloat(value);
							} else if(name.equals("radius")){
								lightProperties.radius = Float.parseFloat(value);
							} else if(name.equals("z")){
								lightProperties.z = Float.parseFloat(value);
							} else if(name.equals("castShadows")){
								lightProperties.castShadows = Boolean.parseBoolean(value);
							}
						}
						fileToLoad.createLightAt(creationPosition.x, creationPosition.y, lightProperties);
					}
				} else if(chunkType.equals(COLLIDER_DATA_CHUNK_ID)){
					if(parsedChunkID.equals(NULL_CHUNK_ID)){
						String[] tokens = line.split(",");
						for(int j = 0; j < tokens.length; j++){
							String token = tokens[j].trim();
							String name = token.split("=")[0].trim();
							String value = token.split("=")[1].trim();
							if(name.equals("x")){
								creationPosition.x = Float.parseFloat(value);
							} else if(name.equals("y")){
								creationPosition.y = Float.parseFloat(value);
							} else if(name.equals("orientation")){
								colliderProperties.orientation = Float.parseFloat(value);
							} else if(name.equals("type")){
								colliderProperties.type = value;
							} else if(name.equals("size")){
								colliderProperties.size = Float.parseFloat(value);
							} else if(name.equals("groupMask")){
								colliderProperties.groupMask = Long.parseLong(value);
							}
						}
						fileToLoad.createColliderAt(creationPosition.x, creationPosition.y, colliderProperties);
					}
				} else if(chunkType.equals(PICKUP_DATA_CHUNK_ID)){
					if(parsedChunkID.equals(NULL_CHUNK_ID)){
						String[] tokens = line.split(",");
						for(int j = 0; j < tokens.length; j++){
							String token = tokens[j].trim();
							String name = token.split("=")[0].trim();
							String value = token.split("=")[1].trim();
							if(name.equals("x")){
								creationPosition.x = Float.parseFloat(value);
							} else if(name.equals("y")){
								creationPosition.y = Float.parseFloat(value);
							} else if(name.equals("type")){
								pickupProperties.type = value;
							} else if(name.equals("respawnTime")){
								pickupProperties.respawnTime = Integer.parseInt(value);
							} else if(name.equals("value")){
								pickupProperties.value = Integer.parseInt(value);
							}
						}
						fileToLoad.createPickupAt(creationPosition.x, creationPosition.y, pickupProperties);
					}
				}
				
			}
			
			chunkType = NULL_CHUNK_ID;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	static void writeChunkHeader(StringBuilder data, String chunkID){
		data.append("CHUNK=" + chunkID).append("\n");
	}
	
	static void writeChunkData(StringBuilder data, String name, Object value){
		data.append(name + "=" + value.toString()).append("\n");
	}
	
	static void writeChunkDataInline(StringBuilder data, String name, Object value){
		data.append(name + "=" + value.toString()).append(",");
	}
	
	static void writeData(StringBuilder data, Map map, EditorProperties properties){
		//WRITE Map Properties
		writeChunkHeader(data, MAP_PROPERTIES_CHUNK_ID);
			writeChunkData(data, "name", map.name);
		
		//Write Editor Properties
		writeChunkHeader(data, EDITOR_PROPERTIES_CHUNK_ID);
			writeChunkData(data, "cameraX", properties.cameraPosition.x);
			writeChunkData(data, "cameraY", properties.cameraPosition.y);
			writeChunkData(data, "cameraZoomX", properties.cameraZoom.x);
			writeChunkData(data, "cameraZoomY", properties.cameraZoom.y);
			writeChunkData(data, "numDivisions", properties.numDivisions);
			writeChunkData(data, "cellSize", properties.cellSize);
			writeChunkData(data, "snapToGrid", properties.snapToGrid);
			writeChunkData(data, "drawGrid", properties.drawGrid);
			
		//WRITE Terrain Objects
		ArrayList<Terrain> terrainObjects = map.terrainObjects;
		writeChunkHeader(data, TERRAIN_DATA_CHUNK_ID);
			for(int i = 0; i < terrainObjects.size(); i++){
				Terrain t = terrainObjects.get(i);
				float x = t.getTransform().getTranslation().x;
				float y = t.getTransform().getTranslation().y;
				float orientation = t.getTransform().getRotation();
				String path = t.getMaterial().getDiffuse().getFilePath();
				String normalPath = t.getMaterial().getNormal().getFilePath();
				String emissivePath = t.getMaterial().getEmissive().getFilePath();
				int size = t.size;
				String type = t.type;
				writeChunkDataInline(data, "x", x);
				writeChunkDataInline(data, "y", y);
				writeChunkDataInline(data, "orientation", orientation);
				writeChunkDataInline(data, "path", path);
				writeChunkDataInline(data, "normalPath", normalPath);
				writeChunkDataInline(data, "emissivePath", emissivePath);
				writeChunkDataInline(data, "size", size);
				writeChunkDataInline(data, "type", type);
				data.append("\n");
			}
		
		//Write Light Objects
		ArrayList<Light> lights = map.lights;
		writeChunkHeader(data, LIGHT_DATA_CHUNK_ID);
			for(int i = 0; i < lights.size(); i++){
				Light light = lights.get(i);
				float x = light.getPosition().x;
				float y = light.getPosition().y;
				float r = light.color.x;
				float g = light.color.y;
				float b = light.color.z;
				float radius = light.radius;
				float z = light.getPosition().z;
				boolean castShadows = light.castShadows;
				writeChunkDataInline(data, "x", x);
				writeChunkDataInline(data, "y", y);
				writeChunkDataInline(data, "r", r);
				writeChunkDataInline(data, "g", g);
				writeChunkDataInline(data, "b", b);
				writeChunkDataInline(data, "radius", radius);
				writeChunkDataInline(data, "z", z);
				writeChunkDataInline(data, "castShadows", castShadows);
				data.append("\n");
			}
			
		ArrayList<Collider> colliders = map.colliders;
		writeChunkHeader(data, COLLIDER_DATA_CHUNK_ID);
			for(int i = 0; i < colliders.size(); i++){
				Collider collider = colliders.get(i);
				float x = collider.body.getTransform().getTranslation().x;
				float y = collider.body.getTransform().getTranslation().y;
				float orientation = collider.body.getTransform().getRotation();
				String type = collider.type;
				float size = collider.getSize();
				long groupMask = collider.groupMask;
				writeChunkDataInline(data, "x", x);
				writeChunkDataInline(data, "y", y);
				writeChunkDataInline(data, "orientation", orientation);
				writeChunkDataInline(data, "type", type);
				writeChunkDataInline(data, "size", size);
				writeChunkDataInline(data, "groupMask", groupMask);
				data.append("\n");
			}
			
		ArrayList<Pickup> pickups = map.pickups;
		writeChunkHeader(data, PICKUP_DATA_CHUNK_ID);
			for(int i = 0; i < pickups.size(); i++){
				Pickup pickup = pickups.get(i);
				
				float x = pickup.getBody().getTransform().getTranslation().x;
				float y = pickup.getBody().getTransform().getTranslation().y;
				String type = "";
				int respawnTime = 0;
				int value = 0;
				
				if(pickup instanceof WeaponPickup){
					WeaponPickup wPickup = (WeaponPickup) pickup;
					type = "Weapon";
					respawnTime = wPickup.respawnTime;
					value = wPickup.weaponID;
				} else if(pickup instanceof HealthPickup){
					HealthPickup hPickup = (HealthPickup) pickup;
					type = "Health";
					respawnTime = hPickup.respawnTime;
					value = hPickup.healthAmount;
				}
				
				writeChunkDataInline(data, "x", x);
				writeChunkDataInline(data, "y", y);
				writeChunkDataInline(data, "type", type);
				writeChunkDataInline(data, "respawnTime", respawnTime);
				writeChunkDataInline(data, "value", value);
				data.append("\n");
			}
		
	}
	
	public static void write(String mapName, Map fileToWrite, EditorProperties properties){
		//TODO Write map data
		Path mapPath = Paths.get(MAP_RESOURCE_PATH, mapName + "." + MAP_RESOURCE_EXT);
		if(!Files.exists(mapPath)){
			try {
				Files.createFile(mapPath);
				System.out.println("[MapIO]["+mapName+"] Map File Created.");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Files.delete(mapPath);
				Files.createFile(mapPath);
				System.out.println("[MapIO]["+mapName+"] Map File Overwritten.");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		StringBuilder data = new StringBuilder();
		writeData(data, fileToWrite, properties);
		
		BufferedWriter writer;
		try {
			writer = Files.newBufferedWriter(mapPath);
			writer.write(data.toString());
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	static void findMaps(ArrayList<String> filenames, Path path){
		
		DirectoryStream<Path> stream;
		
		if(Files.isDirectory(path)){
			try {
				stream = Files.newDirectoryStream(path);
				for(Path entry : stream){
					findMaps(filenames, entry);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			String fileName = path.getName(path.getNameCount() - 1).toString();
			String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
			if(fileExt.equalsIgnoreCase(MAP_RESOURCE_EXT)){
				System.out.println("[MapIO]["+fileName+"] Map File Found.");
				filenames.add(fileName);
			}
		}
	}
	
	public static void getAvailableMaps(ArrayList<String> mapNames){
		Path mapDirectory = Paths.get(".", MAP_RESOURCE_PATH);
		findMaps(mapNames, mapDirectory);
	}
	
}
