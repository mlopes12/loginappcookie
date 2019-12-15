# loginappcookie

Soluciones:
En primer lugar, no es conveniente guardar la contraseña como string, por lo tanto se hace la comparación como array de caracteres en validaPass.
En segundo lugar, además del nombre de usuario, conviene guardar otro parámetro, en este caso será una cadena aleatoria.
En tercer lugar, pasamos a guardar la contraseña para poder entrar con una clave de hash en dos ficheros, que compararemos con nuestra contraseña introducida. Estos ficheros se crean en la carpeta del servidor por un tema de permisos.

Para la compilación de este servicio web se ha utilizado Eclipse. Y para probar el mismo el servidor Tomcat.
