package com.curso.gymv0;

public class Staff extends Usuario{

	public Staff(String nombre) {
		super(nombre);
	}
@Override
public void permisosUsuario() {
	System.out.println("Matricula |"+ id +"| Staff:" + getNombre()+ " |Totalidad de permisos|\n");

}
public void Sistema(){
	System.out.println(""+ getNombre() + "Accedió al sistema Smartfit");
}
}
