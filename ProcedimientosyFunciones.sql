use Polipases;

#a Procedimiento que liste los jugadores por club.

drop view if exists jugadoresPorClub;
create view jugadoresPorClub as
select Equipo.nombreEquipo, Jugador.nombreJugador, Jugador.apellidoJugador from Jugador 
inner join Posicion on Jugador.Posicion_idPosicion = Posicion.id 
inner join Equipo_has_Posicion on Posicion.id = Equipo_has_Posicion.idPosicion
inner join Equipo on Equipo_has_Posicion.idPosicion = Equipo.posicion_id;
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
    select count(Jugador.DNI) into cant from Jugador 
    inner join Posicion on Jugador.Posicion_idPosicion = Posicion.id 
	inner join Equipo_has_Posicion on Posicion.id = Equipo_has_Posicion.idPosicion
	inner join Equipo on Equipo_has_Posicion.idPosicion = Equipo.posicion_id 
    where Posicion.rol = posicionP and Equipo.nombreEquipo = club;
    return cant;
end//
delimiter ;
select cantJugadoresPorPosicion("DFC","River") as cant;

#c.Función que dado un manager y un club retorne la cantidad de jugadores que representa.


delimiter // 

drop function if exists jugadoresManagerClub//
create function jugadoresManagerClub(idManager int, club varchar(45))
returns INT
DETERMINISTIC
begin
	DECLARE cant INT default 0;
    select count(jugador.DNI) into cant from Jugador
    inner join Representante on Jugador.Representante_DNI = Representante.dniRepresentante 
	inner join Representante_has_Equipo on Representante.dniRepresentante = Representante_has_Equipo.Representante_DNI
	inner join Equipo on Representante_has_Equipo.Representante_DNI = Equipo.posicion_id
    where Representante.dniRepresentante = idManager and Equipo.nombreEquipo = club;
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
    #Es completamente innecesario el cursor pero bueno
	DECLARE cur CURSOR FOR select DNI,
    nombreJugador, 
    apellidoJugador ,
    fechaNacimiento ,
    salario ,
    Representante_DNI ,
    Posicion_idPosicion ,
    Fichaje_idFichaje  
    from Jugador order by salario desc;
    open cur;
    fetch cur into dniI, nombre, apellido, nacimiento, salarioAlto, repDNI, rolId, fichajeId;
	set mayorSalario = concat("DNI: ", dniI, ", Nombre: ", nombre, ", Apellido: ", apellido,
    ", Nacimiento: ", nacimiento, ", Salario: ",  salarioAlto, ", Dni Representante:", repDNI,
    ", Id posicion: ", rolId, ", Id fichaje: ",fichajeId);
    close cur;
end//
delimiter ;
-- insert into Jugador(DNI,nombreJugador,apellidoJugador,fechaNacimiento,salario,Representante_DNI,Posicion_idPosicion,Fichaje_idFichaje)
-- values (1,"Juan","Domingues",'2005-10-5',5000.50,2,3,4);
call salarioMayor(@mayorSalario);
select @mayorSalario;

#e.Función que, dada una posición, devuelva el nombre del club con más jugadores de esa posición.

delimiter //
drop function if exists clubMasJugadores//
create function clubMasJugadores(posicionId int)
returns varchar(100)
deterministic
begin
	declare club varchar(100);
	select nombreEquipo, count(*) as cant from Equipo join Equipo_has_Posicion on Equipo_id=idEquipo where idPosicion=posicionId  group by nombreEquipo order by cant desc limit 1 into club ; 
	return club;
end//
delimiter ;
select clubMasJugadores(3) as nombreclub;
#Esto no funciona / update / solo tira un error de q el select tiene cantidad diferentes de columnas
/* select clubMasJugadores(2) as clubMasDFC;
#pero esto creo que si
select nombreEquipo, count(*) as cant from equipo group by nombreEquipo order by cant desc limit 1;
#No encontramos el problema
*/
#f.Procedimiento que, dado un fichaje, lo revise y solucione.
#la idea es crear 3 procedimientos uno q verifique si el manager tienen hablitado el equipo, otro que se fije si el jugador tiene un numero de camiseta que ya esta en el equipo y otro que verifique si la posicion del jugador no exede el maximo de posiciones en el equipo.
# cree un procedimiento que verifique si el manager tiene habilitado el equipo al cual el jugador tiene que ser fichado 

delimiter //
create function verificarManager (idDeFichaje int)
returns boolean 
deterministic
begin 
	declare verificacion boolean default false;
	declare idEquipoFichaje int;
    select Equipo_id from Fichaje where idFichaje=idDeFichaje into idEquipoFichaje;
    if (select Representante_habilitado from Representante_has_Equipo where Equipo_idEquipo=idEquipoFichaje)
		then 
        set verificacion = true;
	end if;
    return verificacion;
end //
delimiter ;

create function verificarPosicion (idDeFichaje int)
returns boolean 
deterministic
begin 
	declare verificacion boolean default true;
	declare idPosicionFichaje int;
    declare idposicionJugador int;
    declare idJugador int;
    declare cantidadDePos int;
    declare idEquipoFichaje int;
	select Equipo_id from Fichaje where idFichaje=idDeFichaje into idEquipoFichaje;
    select Jugador.Posicion_idPosicion from Jugador join Fichaje on Jugador.DNI=Jugador_DNI  where idFichaje=idDeFichaje into idPosicionFichaje;
    select Fichaje.Jugador_DNI from Fichaje where idFichaje=idDeFichaje into idJugador;
    select Posicion_idPosicion from Jugador where DNI= idJugador into idposicionJugador;
    select count(*) from (select posicion_id from Equipo where idEquipo = idEquipoFichaje and posicion_id=idPosicionFichaje) into cantidadDePos;
    if (cantidadDePos = cantidadPermitida)
		then 
        set verificacion = false;
	end if;
    return verificacion;
end //
delimiter ;



