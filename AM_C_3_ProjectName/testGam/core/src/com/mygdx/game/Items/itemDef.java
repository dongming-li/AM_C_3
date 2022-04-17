package com.mygdx.game.Items;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by lebeau on 11/7/2017.
 */

public class itemDef {



        public Vector2 position;
        public Class<?> type;

        public itemDef(Vector2 position, Class<?> type){
            this.position = position;
            this.type = type;
        }

    }
