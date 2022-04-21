/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package server;

import java.util.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import server.server_classes.AbstractGame;
import server.server_classes.IdGenerator;

@Configuration
public class Config {

    /**
     * Bean for creating a Random object
     * @return the Random object
     */
    @Bean
    public Random getRandom() {
        return new Random();
    }

    /**
     * Bean for creating a game map. (SHARES THE SAME INSTANCE ACROSS THE APPLICATION)
     *
     * @return The Map containing the ids and the game
     */
    @Bean
    @Scope("singleton")
    public Map<Long, AbstractGame> gameMap() {
        return new HashMap<>();
    }

    /**
     * Bean for IdGenerator. (SHARES THE SAME INSTANCE ACROSS THE APPLICATION)
     *
     * @return An instance of idGenerator
     */
    @Bean
    @Scope("singleton")
    public IdGenerator idGenerator() {
        return new IdGenerator();
    }

}