/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample.model;

import java.util.Objects;

/**
 *
 * @author lendle
 */
public class Edge {
    private Vertex vertex1=null;
    private Vertex vertex2=null;

    public Edge(Vertex v1, Vertex v2){
        if(v1.x<v2.x){
            this.vertex1=v1;
            this.vertex2=v2;
        }else if(v1.x==v2.x && v1.y<v2.y){
            this.vertex1=v1;
            this.vertex2=v2;
        }else if(v1.x==v2.x && v1.y==v2.y && v1.z<v2.z){
            this.vertex1=v1;
            this.vertex2=v2;
        }else{
            this.vertex1=v2;
            this.vertex2=v1;
        }
    }
    
    public Vertex getVertex1() {
        return vertex1;
    }

    public void setVertex1(Vertex vertex1) {
        this.vertex1 = vertex1;
    }

    public Vertex getVertex2() {
        return vertex2;
    }

    public void setVertex2(Vertex vertex2) {
        this.vertex2 = vertex2;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.vertex1);
        hash = 67 * hash + Objects.hashCode(this.vertex2);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Edge other = (Edge) obj;
        if (!Objects.equals(this.vertex1, other.vertex1)) {
            return false;
        }
        if (!Objects.equals(this.vertex2, other.vertex2)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Edge{" + "vertex1=" + vertex1 + ", vertex2=" + vertex2 + '}';
    }
    
    
}
