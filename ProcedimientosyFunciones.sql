use Polipases;
#a Procedimiento que liste los jugadores por club.

drop view if exists jugadoresPorClub;
create view jugadoresPorClub as
select equipo.nombreEquipo, jugador.nombreJugador, jugador.apellidoJugador from jugador 
inner join posicion on jugador.Posicion_idPosicion = posicion.id 
inner join equipo_has_posicion on posicion.id = equipo_has_posicion.idPosicion
inner join equipo on equipo_has_posicion.idPosicion = equipo.posicion_id;
#El group by no lo vemos necesario en este caso si quieren agreguen inserts para verificar o cambien lo que este mal
select * from jugadoresPorClub;

#b Función que, dada una posición y un club, retorne la cantidad de jugadores convocados para esa posición.

delimiter // 

drop function if exists cantJugadoresPorPosicion//
create function cantJugadoresPorPosicion(posicionP varchar(45), club varchar(45))
returns INT
DETERMINISTIC
begin
	DECLARE cant INT default 0;
    select count(jugador.DNI) into cant from jugador 
    inner join posicion on jugador.Posicion_idPosicion = posicion.id 
	inner join equipo_has_posicion on posicion.id = equipo_has_posicion.idPosicion
	inner join equipo on equipo_has_posicion.idPosicion = equipo.posicion_id 
    where posicion.rol = posicionP and equipo.nombreEquipo = club;
    return cant;
end//

delimiter ;

select cantJugadoresPorPosicion("DFC","River") as cant;

#c.Función que dado un manager y un club retorne la cantidad de jugadores que representa.


delimiter // 

drop function if exists jugadoresManagerClub//
create function jugadoresManagerClub(manager varchar(45), club varchar(45))
returns INT
DETERMINISTIC
begin
	DECLARE cant INT default 0;
    select count(jugador.DNI) into cant from jugador
    inner join representante on jugador.Representante_DNI = representante.dniRepresentante 
	inner join representante_has_equipo on representante.dniRepresentante = representante_has_equipo.Representante_DNI
	inner join equipo on representante_has_equipo.Representante_DNI = equipo.posicion_id
    where representante.nombreRepresentante = manager and equipo.nombreEquipo = club;
    return cant;
end//

delimiter ;

select jugadoresManagerClub("","") as cantJugadores;

#d. Procedimiento que retorne en parámetros de salida los datos del jugador mejor pago.

delimiter //

drop procedure if exists salarioMayor//
create procedure salarioMayor(OUT mayorSalario varchar(1000))
begin
	DECLARE dniI int;
    DECLARE nombre varchar(45);
    DECLARE apellido varchar(45);
    DECLARE nacimiento date;
    DECLARE salarioAlto DECIMAL(10,2);
    DECLARE repDNI int;
    DECLARE rolId int;
    DECLARE fichajeId int;
    #Es completamente inecesario el cursor pero bueno
	DECLARE cur CURSOR FOR select DNI,
    nombreJugador, 
    apellidoJugador ,
    fechaNacimiento ,
    salario ,
    Representante_DNI ,
    Posicion_idPosicion ,
    Fichaje_idFichaje  
    from jugador order by salario desc;
    open cur;
    fetch cur into dniI, nombre, apellido, nacimiento, salarioAlto, repDNI, rolId, fichajeId;
	set mayorSalario = concat("DNI: ", dniI, ", Nombre: ", nombre, ", Apellido: ", apellido,
    ", Nacimiento: ", nacimiento, ", Salario: ",  salarioAlto, ", Dni Representante:", repDNI,
    ", Id posicion: ", rolId, ", Id fichaje: ",fichajeId);
    close cur;
end//

delimiter ;

#insert into jugador(DNI,nombreJugador,apellidoJugador,fechaNacimiento,salario,Representante_DNI,Posicion_idPosicion,Fichaje_idFichaje)
#values (1,"Juan","Domingues",'2005-10-5',5000.50,2,3,4);
call salarioMayor(@mayorSalario);
select @mayorSalario;

#e.Función que, dada una posición, devuelva el nombre del club con más jugadores de esa posición.

delimiter //

drop function if exists clubMasJugadores//
create function clubMasJugadores(posicionId varchar(45))
returns varchar(100)
deterministic
begin
	declare club varchar(100);
	select nombreEquipo, count(*) as cant from equipo group by nombreEquipo order by cant desc limit 1 into club; 
	return club;
end//

delimiter ;
#Esto no funciona
select clubMasJugadores(2) as clubMasDFC;
#pero esto creo que si
select nombreEquipo, count(*) as cant from equipo group by nombreEquipo order by cant desc limit 1;
#No encontramos el problema

#f.Procedimiento que, dado un fichaje, lo revise y solucione.

