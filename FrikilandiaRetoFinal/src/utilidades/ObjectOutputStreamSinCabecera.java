package utilidades;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
public class ObjectOutputStreamSinCabecera extends ObjectOutputStream{

		@Override
		protected void writeStreamHeader() throws IOException {
		 reset();
	 }

		//Constructores
	public ObjectOutputStreamSinCabecera () throws IOException{
		super();
		}

		public ObjectOutputStreamSinCabecera(OutputStream out) throws IOException{
		super(out);
		}
	}