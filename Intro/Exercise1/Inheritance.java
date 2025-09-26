public  class Inheritance {
    public  static  void main(String[] args){

    }
}

class Shape {
    void area(){

    }
}

class Circle extends  Shape {
    private double radius;

    public Circle(double radius){
        this.radius=radius;
    }

    public double area(){
        return Math.PI*radius*radius;

    }

    public double getRadius(){return radius}
}

class Rectangle extends  Shape{
    private double length;
    private double width;
    Rectangle(double length, double width){
        this.length=length;
        this.width=width;
    }

    public double area(){return length*width;}

    public double getLength(){return length;}
    public double getWidth(){return width;}
}