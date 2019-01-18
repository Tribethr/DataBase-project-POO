En caso de querer utilizar la base de datos esta es una opcion para crear tanto como la base de datos, sus respectivas tablas y las respectivas filas o atributos de cada tabla. 
    
BaseDeDatos base = new BaseDeDatos("Estudiantes");

Tabla tabla1 = new Tabla("Nuevos ingresos");

Tabla tabla2 = new Tabla("Quedados");

Tabla tabla3= new Tabla("Aprobados");

Tabla tabla4 = new Tabla("Graduados");

Tabla[] tablas = {tabla1,tabla2,tabla3,tabla4};

Fila<String> nombre = new Fila<Integer>("Nombre","String",true);

Fila<Integer> carnet = new Fila<Integer>("Carnet","Int",true);

Fila<Integer> celular = new Fila<Integer>("Celular","Int",false);

Fila[] filas = {nombre,carnet,celular};

for(Tabla tabla: tablas) {

    for(Fila fila: filas) {

		tabla.annadirFila(fila);

	}

	tabla.crearMensajeDeLaEstructura();

	base.annadirTabla(tabla);

}