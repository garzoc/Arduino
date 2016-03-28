/*
 * SortedList.cpp
 *
 *  Created on: Mar 26, 2016
 *      Author: john
 */

#include "SortedList.h"

SortedList::SortedList(int maxSize) {
	this->closedNodes=new PathPane *[maxSize];
	this->size=0;
}

SortedList::~SortedList() {
	for(int i=0;i<this->getSize();i++){
		delete this->closedNodes[i];
	}
	delete[] this->closedNodes;
	this->closedNodes=0;
}

/*
 * Doesn't properly handle copies
 * */
void SortedList::add(PathPane node){
	//PathPane **nodes=new PathPane(**this->closedNodes);
	PathPane *placeHolderNode;
	PathPane *tempNode;
	//store the first node in a place holder if we know that there is a first node
	if(this->getSize()>0){
		placeHolderNode=new PathPane(*this->closedNodes[0]);
	}
	//tells us if the node have already been merged once
	bool nodeImpl=false;
	for(int i=0;i<this->getSize();i++){
		//this->closedNodes[i]=new PathPane((nodes[i]->getID()<node.getID())?*nodes[i]:node);
		if(placeHolderNode->getID()>node.getID()&&!nodeImpl){
			nodeImpl=true;
			tempNode=new PathPane(*this->closedNodes[i]);
			this->closedNodes[i]=new PathPane(node);
			placeHolderNode=new PathPane(*tempNode);
		}else{
			tempNode=new PathPane(*this->closedNodes[i]);
			this->closedNodes[i]=new PathPane(*placeHolderNode);
			//store different nodes in the placeholder depending on if the node argument has ben used or not
			//and the next node must exist
			if(!nodeImpl&&i+1<this->getSize()){
				placeHolderNode=new PathPane(*this->closedNodes[i+1]);
			}else{
				placeHolderNode=new PathPane(*tempNode);
			}
		}
	}
	if(this->getSize()>0){
		if(this->closedNodes[this->getSize()-1]->getID()==placeHolderNode->getID()){
			this->closedNodes[this->getSize()]=new PathPane(node);
		}else{
			this->closedNodes[this->getSize()]=new PathPane(*placeHolderNode);
		}
		delete placeHolderNode;
		delete tempNode;
	}else{
		this->closedNodes[0]=new PathPane(node);
	}
	this->size++;



}

int SortedList::exists(PathPane node){

	double branchSize =ceil((double)(this->getSize() - 1) / 2);
	int location = branchSize;
	for (int i = 0; i < this->getSize() && 0 <= location && location < this->getSize(); i++) {
		//std::cout<<location<<" Positoin  "<< branchSize<<" Branch"<<std::endl;
		//std::cout<<node.getID()<<" VS "<<this->closedNodes[location]->getID()<<std::endl;
		if (node.getID()<this->closedNodes[location]->getID()) {
			branchSize = ceil( branchSize / 2);
			location -= branchSize;
		} else if (node.getID()>this->closedNodes[location]->getID()) {
			//std::cout<<ceil(branchSize / 2)<<std::endl;
			branchSize =ceil( branchSize / 2);
			location += branchSize;
		} else if (node.getID()==this->closedNodes[location]->getID()) {
			return location;
		}
	}
	return -1;
}


PathPane SortedList::get(int x){
	return *this->closedNodes[x];
}

int SortedList::getSize(){
	return this->size;
}

