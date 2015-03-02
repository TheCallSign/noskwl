/*
 * Copyright (C) 2015 giddyc
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package net.strawberrystudios.noskwl.packet;

import java.lang.reflect.Type;


/**
 *
 * @author giddyc
 */
public class Data<T> {

    private final Type type;
    private final T data;
    public Data(Type t, T data){
        this.type = t;
        this.data = data;
    }
    
    public T getData(){
        return data;
    }
    
    public Type getType(){
        return this.type;
    }
}
