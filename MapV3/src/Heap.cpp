/*
 * Heap.cpp
 *
 *  Created on: Mar 25, 2016
 *      Author: john
 */

#include "Heap.h"

Heap::Heap(int maxHeapSize) {
	this->size=0;
	this->nodes=new PathPane *[maxHeapSize];

	// TODO Auto-generated constructor stub

}

Heap::~Heap() {
	//this for-loop might cause problems

	for(int i=0;i<this->getSize();i++){
		delete this->nodes[i];
	}
	delete[] this->nodes;
	this->nodes=0;
}


void Heap::add(PathPane node){
	this->sortUp(node);
	this->size++;
}


void Heap::sortUp(PathPane node){
	this->nodes[this->getSize()]=new PathPane(node);
	int childIndex=this->getSize();
	int parentIndex=floor(((double)this->getSize()-1)/2);
	while(0<parentIndex){
		if(this->nodes[childIndex]->getF()<this->nodes[parentIndex]->getF()){
			this->swap(parentIndex,childIndex);
			childIndex=parentIndex;
			parentIndex=floor(((double)parentIndex-1)/2);
		}else{
			break;
		}
	}
	if(parentIndex>=0){
		if(this->nodes[childIndex]->getF()<this->nodes[parentIndex]->getF()){
			this->swap(parentIndex,childIndex);

		}
	}
}

void Heap::swap(int parent ,int child){
	PathPane parentNode=*this->nodes[parent];
	PathPane childNode=*this->nodes[child];
	this->nodes[parent]=new PathPane(childNode);
	this->nodes[child]=new PathPane(parentNode);
}

PathPane Heap::poll(){
	this->size--;
	return this->sortDown();

}

PathPane Heap::sortDown(){
	this->swap(0,this->size);
	PathPane tempNode=*this->nodes[this->getSize()];
	this->nodes[this->getSize()]=0;
	delete this->nodes[this->getSize()];
	int parentIndex=0;
	int childIndex=0;
	while(parentIndex*2+1<this->getSize()){
		if(parentIndex*2+2<this->getSize()){
			bool swapable=this->nodes[parentIndex]->getF()>nodes[childIndex=((nodes[parentIndex*2+1]->getF()<nodes[parentIndex*2+2]->getF())?parentIndex*2+1:parentIndex*2+2)]->getF()?true:false;
			if(swapable){
				swap(parentIndex,childIndex);
				parentIndex=childIndex;
			}else{
				break;
			}
		}else{
			bool swapable= this->nodes[parentIndex]->getF()>this->nodes[parentIndex*2+1]->getF()?true:false;
			if(swapable){
				swap(parentIndex,childIndex=parentIndex*2+1);
				parentIndex=childIndex;
			}else{
				break;
			}
		}
	}
	return tempNode;
}

PathPane Heap::peek(){
	return *this->nodes[0];
}

int Heap::getSize(){
	return this->size;
}

void Heap::printHeapF(){
	for(int i=0;i<this->getSize();i++){
		std::cout<<this->nodes[i]->getF()<<" ";
	}
	std::cout<<"\n";
}

