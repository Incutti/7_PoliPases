use PoliPases;
/*
INSERT INTO `PoliPases`.`Equipo` (`idEquipo`, `nombreEquipo`) VALUES
(1, 'Equipo A'),
(2, 'Equipo B'),
(3, 'Equipo C');
INSERT INTO `PoliPases`.`Posicion` (`id`, `rol`) VALUES
(1, 'Delantero'),
(2, 'Centrocampista'),
(3, 'Defensa');
INSERT INTO `PoliPases`.`Equipo_has_Posicion` (`Equipo_id`, `idPosicion`, `cantidadPermitida`) VALUES
(1, 1, 3),
(1, 2, 4),
(2, 1, 2),
(2, 3, 3),
(3, 2, 5);
INSERT INTO `PoliPases`.`Fichaje` (`idFichaje`, `numCamiseta`, `fechaHoraFichaje`, `Equipo_id`, `Jugador_id`) VALUES
(1, '10', '2023-06-01 10:00:00', 1,11111111),
(2, '7', '2023-06-02 15:30:00', 2,22222222),
(3, '5', '2023-06-03 12:45:00', 3,33333333);
INSERT INTO `PoliPases`.`Representante` (`dniRepresentante`, `nombreRepresentante`, `apellidoRepresentante`, `fechaNacimiento`) VALUES
(12345678, 'Juan', 'Pérez', '1990-05-15'),
(87654321, 'María', 'Gómez', '1985-12-10');
INSERT INTO `PoliPases`.`Jugador` (`DNI`, `nombreJugador`, `apellidoJugador`, `fechaNacimiento`, `salario`, `Representante_DNI`, `Posicion_idPosicion`) VALUES
(11111111, 'Jugador A', 'Apellido A', '1995-08-20', 5000.00, 12345678, 1),
(22222222, 'Jugador B', 'Apellido B', '1998-02-10', 4000.00, 87654321, 2),
(33333333, 'Jugador C', 'Apellido C', '1993-11-05', 6000.00, 12345678, 3);
INSERT INTO `PoliPases`.`Representante_has_Equipo` (`Representante_DNI`, `Equipo_idEquipo`, `Representante_habilitado`) VALUES
(12345678, 1, 1),
(87654321, 2, 1),
(12345678, 3, 0);
*/

#a Procedimiento que liste los jugadores por club.

drop view if exists jugadoresPorClub;
create view jugadoresPorClub as
select Equipo.nombreEquipo, Jugador.nombreJugador, Jugador.apellidoJugador from Jugador 
join Fichaje on Jugador.DNI = Fichaje.Jugador_id
join Equipo on Fichaje.Equipo_id = Equipo.idEquipo;
select * from jugadoresPorClub;
select * from Equipo;
#b Función que, dada una posición y un club, retorne la cantidad de jugadores convocados para esa posición.

delimiter // 

drop function if exists cantJugadoresPorPosicion//
create function cantJugadoresPorPosicion(posicionP int, idclub int)
returns INT
DETERMINISTIC
begin
	DECLARE cant INT default 0;
    select count(Jugador.DNI) into cant from Jugador 
    left join Posicion on Jugador.Posicion_idPosicion = Posicion.id 
	left join Equipo_has_Posicion on Posicion.id = Equipo_has_Posicion.idPosicion
    where Jugador.Posicion_idPosicion = posicionP and Equipo_has_Posicion.Equipo_id = idclub;
    return cant;
end//
delimiter ;
select cantJugadoresPorPosicion(1,1) as cantJugadoresPorPosicion;
#c.Función que dado un manager y un club retorne la cantidad de jugadores que representa.


delimiter // 

drop function if exists jugadoresManagerClub//
create function jugadoresManagerClub(idManager int, club int)
returns INT
DETERMINISTIC
begin
	DECLARE cant INT default 0;
    select count(Jugador.DNI) into cant from Jugador
    left join Representante on Jugador.Representante_DNI = Representante.dniRepresentante 
	left join Representante_has_Equipo on Representante.dniRepresentante = Representante_has_Equipo.Representante_DNI
    where Representante.dniRepresentante = idManager and Representante_has_Equipo.Equipo_idEquipo = club;
    return cant;
end//

delimiter ;

select jugadoresManagerClub(12345678,1) as jugadoresManagerClub;

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
	select DNI,
    nombreJugador, 
    apellidoJugador ,
    fechaNacimiento ,
    salario ,
    Representante_DNI ,
    Posicion_idPosicion ,
    Fichaje_idFichaje  
    from Jugador order by salario desc limit 1
    into dniI, nombre, apellido, nacimiento, salarioAlto, repDNI, rolId, fichajeId;
	set mayorSalario = concat("DNI: ", dniI, ", Nombre: ", nombre, ", Apellido: ", apellido,
    ", Nacimiento: ", nacimiento, ", Salario: ",  salarioAlto, ", Dni Representante:", repDNI,
    ", Id posicion: ", rolId, ", Id fichaje: ",fichajeId);
end//
delimiter ;
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
	select Equipo.nombreEquipo into club from Equipo
    inner join Equipo_has_Posicion on Equipo_has_Posicion.Equipo_id=Equipo.idEquipo 
    inner join Posicion on Equipo_has_Posicion.idPosicion=Posicion.id
    inner join Jugador on Posicion.id = Jugador.Posicion_idPosicion
    where Jugador.Posicion_idPosicion=posicionId  group by Equipo.nombreEquipo 
    order by count(Jugador.DNI) desc limit 1; 
	return club;
end//
delimiter ;
select clubMasJugadores(3) as clubMasJugadores;

#f.Procedimiento que, dado un fichaje, lo revise y solucione.
#la idea es crear 3 funciones uno q verifique si el manager tienen hablitado el equipo, otro que se fije si el jugador tiene un numero de camiseta que ya esta en el equipo y otro que verifique si la posicion del jugador no excede el maximo de posiciones en el equipo.
# cree un procedimiento que verifique si el manager tiene habilitado el equipo al cual el jugador tiene que ser fichado 

delimiter //
drop function if exists verificarManager//
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

delimiter //
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



