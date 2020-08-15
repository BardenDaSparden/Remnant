//package com.remnant.graphics;
//
//import java.nio.FloatBuffer;
//
//import org.joml.Matrix3f;
//import org.joml.Matrix4f;
//import org.joml.Vector2f;
//import org.joml.Vector3f;
//import org.joml.Vector4f;
//import org.lwjgl.BufferUtils;
//
//import com.remnant.resources.ShaderLoader;
//
//import static org.lwjgl.opengl.GL20.*;
//
//public class Shader {
//
//	static FloatBuffer BUFFER_3X3 = BufferUtils.createFloatBuffer(9);
//	static FloatBuffer BUFFER_4X4 = BufferUtils.createFloatBuffer(16);
//	
//	String vertexPath;
//	String fragmentPath;
//	
//	int vertexShader;
//	int fragmentShader;
//	int programHandle;
//	
//	public Shader(String vertexPath, String fragmentPath){
//		this.vertexPath = vertexPath;
//		this.fragmentPath = fragmentPath;
//		vertexShader = ShaderLoader.getShader(vertexPath);
//		fragmentShader = ShaderLoader.getShader(fragmentPath);
//		programHandle = glCreateProgram();
//		glAttachShader(programHandle, vertexShader);
//		glAttachShader(programHandle, fragmentShader);
//		//glValidateProgram(programHandle);
//		glLinkProgram(programHandle);
//		
//		String programInfo = glGetProgramInfoLog(programHandle, 1024);
//		System.out.println(programInfo);
//	}
//	
//	int getLocation(String uName){
//		return glGetUniformLocation(programHandle, uName);
//	}
//	
//	public void bind(){
//		glUseProgram(programHandle);
//	}
//	
//	public void release(){
//		glUseProgram(0);
//	}
//	
//	public void dispose(){
//		glDeleteProgram(programHandle);
//	}
//	
//	public void setFloat(String name, float f){
//		int loc = getLocation(name);
//		glUniform1f(loc, f);
//	}
//	
//	public void setVector2(String name, Vector2f v){
//		int loc = getLocation(name);
//		glUniform2f(loc, v.x, v.y);
//	}
//	
//	public void setVector3(String name, Vector3f v){
//		int loc = getLocation(name);
//		glUniform3f(loc, v.x, v.y, v.z);
//	}
//	
//	public void setVector4(String name, Vector4f v){
//		int loc = getLocation(name);
//		glUniform4f(loc, v.x, v.y, v.z, v.w);
//	}
//	
//	public void setMatrix3(String name, Matrix3f mat){
//		int loc = getLocation(name);
//		BUFFER_3X3.clear();
//		mat.get(0, BUFFER_3X3);
//		glUniformMatrix3(loc, false, BUFFER_3X3);
//	}
//	
//	public void setMatrix4(String name, Matrix4f mat){
//		int loc = getLocation(name);
//		BUFFER_4X4.clear();
//		mat.get(0, BUFFER_4X4);
//		glUniformMatrix4(loc, false, BUFFER_4X4);
//	}
//	
//	public void setTexture(String name, Texture texture, int unit){
//		int loc = getLocation(name);
//		texture.bind(unit);
//		glUniform1i(loc, unit);
//	}
//	
//}
package com.remnant.graphics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import org.barden.util.Debug;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import com.remnant.assets.Texture;
import com.remnant.math.Transform;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32.*;
import static org.lwjgl.opengl.GL40.*;

public abstract class Shader {

	private static FloatBuffer BUFFER_16 = BufferUtils.createFloatBuffer(16);
	private static FloatBuffer BUFFER_9 = BufferUtils.createFloatBuffer(9);
	
	private int programID;
	
	private int vertexShaderID;
	private int fragmentShaderID;
	private int geometryShaderID;
	private int tesselationControlShaderID;
	private int tesselationEvaluationShaderID;
	
	public Shader(String vertexPath, String fragmentPath){
		this(vertexPath, fragmentPath, null, null, null);
	}
	
	public Shader(String vertexPath, String fragmentPath, String geometryShaderPath, String tessControlShaderPath, String tessEvalShaderPath){
		this.vertexShaderID = loadShader(vertexPath, GL_VERTEX_SHADER);
		this.fragmentShaderID = loadShader(fragmentPath, GL_FRAGMENT_SHADER);
		this.geometryShaderID = loadShader(geometryShaderPath, GL_GEOMETRY_SHADER);
		this.tesselationControlShaderID = loadShader(tessControlShaderPath, GL_TESS_CONTROL_SHADER);
		this.tesselationEvaluationShaderID = loadShader(tessEvalShaderPath, GL_TESS_EVALUATION_SHADER);
		this.programID = glCreateProgram();
		
		glAttachShader(programID, vertexShaderID);
		glAttachShader(programID, fragmentShaderID);
		if(geometryShaderID != -1){
			glAttachShader(programID, geometryShaderID);
		}
		if(tesselationControlShaderID != -1){
			glAttachShader(programID, tesselationControlShaderID);
		}
		if(tesselationEvaluationShaderID != -1){
			glAttachShader(programID, tesselationEvaluationShaderID);
		}
		
		glValidateProgram(programID);
		glLinkProgram(programID);
		
		String infoLog = glGetProgramInfoLog(programID, 1024);
		int li = vertexPath.lastIndexOf('/');
		int li2 = vertexPath.lastIndexOf('.');
		String shaderName = vertexPath.substring(li + 1, li2);
		
		if(infoLog.length() > 2){
			Debug.logInfo("Resources", "Shader Info: " + shaderName + "\n" + infoLog);
		}
		//Debug.logInfo("resources", "Shaders -> Load -> " + shaderName);
	}
	
	public abstract void setMatrices(Camera camera, Transform transform);
	public abstract void setMaterial(Material material);
	public abstract void updateUniforms();
	
	protected void setFloat(String name, float v0){
		glUniform1f(getUniformLocation(name), v0);
	}
	
	protected void setInteger(String name, int i0){
		glUniform1i(getUniformLocation(name), i0);
	}
	
	protected void setBoolean(String name, boolean b0){
		int value = (b0) ? 1 : 0;
		glUniform1i(getUniformLocation(name), value);
	}
	
	protected void setVector(String name, Vector2f v){
		glUniform2f(getUniformLocation(name), v.x, v.y);
	}
	
	protected void setVector(String name, Vector3f v){
		glUniform3f(getUniformLocation(name), v.x, v.y, v.z);
	}
	
	protected void setVector(String name, Vector4f v){
		glUniform4f(getUniformLocation(name), v.x, v.y, v.z, v.w);
	}
	
	protected void setMatrix(String name, Matrix3f matrix){
		BUFFER_9.clear();
		matrix.get(BUFFER_9);
		glUniformMatrix3fv(getUniformLocation(name), false, BUFFER_9);
	}
	
	protected void setMatrix(String name, Matrix4f matrix){
		BUFFER_16.clear();
		matrix.get(BUFFER_16);
		glUniformMatrix4fv(getUniformLocation(name), false, BUFFER_16);
	}
	
	protected void setTexture(String name, Texture texture, int unit){
		texture.bind(unit);
		glUniform1i(getUniformLocation(name), unit);
	}
	
	protected int getUniformLocation(String name){
		return glGetUniformLocation(programID, name);
	}
	
	public void bind(){
		glUseProgram(programID);
	}
	
	private static int loadShader(String filePath, int shaderType){
		int shaderID = -1;
		
		if(filePath == null){
			return shaderID;
		} else {
			InputStream stream = ClassLoader.getSystemResourceAsStream(filePath);
			InputStreamReader sr = new InputStreamReader(stream);
			BufferedReader reader = new BufferedReader(sr);
			
			StringBuilder source = new StringBuilder();
			String line = "";
			
			try {
				while((line = reader.readLine()) != null){
					source.append(line).append("\n");
				}
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			shaderID = glCreateShader(shaderType);
			glShaderSource(shaderID, source.toString());
			glCompileShader(shaderID);
		}
		
		return shaderID;
	}
	
	public void release(){
		glUseProgram(0);
	}
	
	public void delete(){
		glDeleteShader(vertexShaderID);
		glDeleteShader(fragmentShaderID);
		if(geometryShaderID != -1)
			glDeleteShader(geometryShaderID);
		if(tesselationEvaluationShaderID != -1)
			glDeleteShader(tesselationEvaluationShaderID);
		if(tesselationControlShaderID != -1)
			glDeleteShader(tesselationControlShaderID);
		glDeleteProgram(programID);
	}
	
}