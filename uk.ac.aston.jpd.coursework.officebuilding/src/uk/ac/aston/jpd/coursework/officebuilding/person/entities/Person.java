package uk.ac.aston.jpd.coursework.officebuilding.person.entities;

public class Person {
 protected int ID;
 protected int currentFloor;
 protected int weight;
 protected int destination;

 public Person(int weight) {
  this.weight = weight;
 }
 
 protected int getCurrentFloor() {
  return currentFloor;
 }

 public int getDestination() {
  return destination;
  // TODO Auto-generated method stub
 }
 
 protected setDestnation(int floorNo) {
  this.destination = floorNo;
 }
 
 public int time() {
  
 }
 
 public void waitForElevator(int dest) {
  
 }
 
 public void exitElevator(int floorNo) {
  this.destination = floorNo;
 }
 
 public int getWeight() {
  return weight;
 }
}