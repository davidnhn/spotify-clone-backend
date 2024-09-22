package fr.codecake.spotify_clone.usercontext.mapper;

import fr.codecake.spotify_clone.usercontext.ReadUserDTO;
import fr.codecake.spotify_clone.usercontext.domain.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    ReadUserDTO userToReadUserDTO(User user);
}


/*
 * Comment fonctionne MapStruct ?
 *
 * Compilation : Lors de la compilation, MapStruct génère une implémentation de l'interface UserMapper
 * qui contient le code de mapping entre User et ReadUserDTO.
 *
 * Injection de dépendances : Grâce à componentModel = "spring", l'implémentation générée de UserMapper
 * devient un bean Spring. Vous pouvez l'injecter dans vos services ou contrôleurs à l'aide de @Autowired.
 */
