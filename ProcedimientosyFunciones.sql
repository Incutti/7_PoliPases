use Polipases;
#1

create view jugadoresPorClub as
select * from Jugador 
inner join posicion on jugador.Posicion_idPosicion = Posicion.idPosicion 
inner join Equipo_has_Posicion on Posicion.idPosicion = Equipo_has_Posicion.Posicion_idPosicion

#2

delimiter // 

create function (posicion varchar(45), club varchar(45))
returns INT
begin
	
end//

delimiter ;
