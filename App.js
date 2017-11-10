import React from 'react';
import { StyleSheet, Text, View, FlatList, TextInput, Modal, Button, TouchableHighlight, ScrollView } from 'react-native';
import Communications from 'react-native-communications';


export default class App extends React.Component {
    constructor(props) {
        super(props);
        this.state = { name: 'Pet name',
                       breed: 'Pet breed',
                       age: 'Pet age',
                       gender: 'Pet gender',
                       color: 'Pet color',
                        description: 'task description',
                        mainModalVisible: false,
                        taskModalVisible: false,
                        taskDetailModalVisible: false,
                        itemsCount: 0,
                        tasksCount:0,
                        currentPet: -1,
                        currentTask: -1,
                        selected: false,
                        pets: [{key: "Milka", id: 1, breed: "cow", age: 13, gender: "Female", color: "brown and white",},
                            {key: "Coco", id: 2, breed: "parrot", age: 1, gender: "Male", color: "green",},
                            {key: "Lola", id: 3, breed: "cat", age: 3, gender: "Female", color: "black",},
                            {key: "Cocolina", id: 4, breed: "parrot", age: 2, gender: "Female", color: "blue",},
                            {key: "Lucky", id: 5, breed: "dog", age: 4, gender: "Male", color: "gray",},
                            ],
                        tasks: [{id:1, c:1, key:"task1"}, {id:1, c:2,key:"task2"},
                            {id:2,c:3, key:"task3"}, {id:2, c:4,key:"task4"},
                            {id:3, c:5,key:"task5"}, {id:3, c:6,key:"task6"},
                            {id:4, c:7,key:"task7"}, {id:4, c:8,key:"task8"},
                            {id:5, c:9,key:"task9"}, {id:5, c:10,key:"task10"},]};
    }

    setCurrentPetByName(name) {
        var size = this.state.pets.length;

        for(var i = 0; i < size; i++) {
            if(this.state.pets[i].key === name) {
                this.state.currentPet = this.state.pets[i].id;
            }
        }
    }

    getTasksByID() {
        var size = this.state.tasks.length;
        var tasksByID = [];
        for(var i = 0; i < size; i++) {
            if(this.state.tasks[i].id === this.state.currentPet) {
                tasksByID.push({id:this.state.currentPet, key:this.state.tasks[i].key});
            }
        }
        return tasksByID;
    }

    setmainModalVisible(visible) {
        this.setState({mainModalVisible: visible});
    }

    settaskModalVisible(visible) {
        this.setState({taskModalVisible: visible});
    }

    settaskDetailModalVisible(visible) {
        this.setState({taskDetailModalVisible: visible});
    }

    resetState() {
        this.state.name = 'Pet name';
        this.state.breed = 'Pet breed';
        this.state.age = 'Pet age';
        this.state.gender = 'Pet gender';
        this.state.color = 'Pet color';
    }

    resetTask() {
        this.state.description = 'task description';
        this.state.currentTask = -1;
    }

    getStateText() {
        return "Pet: Name=" + this.state.name +
               ", Breed=" + this.state.breed +
               ", Age=" + this.state.age +
               ", Gender=" + this.state.gender +
               ", Color=" + this.state.color;
    }

    isInArray(name) {
        var count = this.state.pets.length;
        for(var i = 0; i < count ; i++) {
            if(this.state.pets[i].key === name) {
                return i;
            }
        }
        return -1;
    }

    updatePet(index) {
        this.state.pets[index].key = this.state.name;
        this.state.pets[index].breed = this.state.breed;
        this.state.pets[index].age = this.state.age;
        this.state.pets[index].gender = this.state.gender;
        this.state.pets[index].color = this.state.color;

    }

    addToPets() {
        if(this.state.name !== 'Pet name' && this.state.breed !== 'Pet breed' &&
            this.state.age !== 'Pet age' && this.state.gender !== 'Pet gender' && this.state.color !== 'Pet color') {

            var index = this.isInArray(this.state.name);
            if(index !== -1) {
                return "already exists";
            }
            this.state.itemsCount = this.state.itemsCount+1;
            this.state.pets.push({
                key: this.state.name,
                id: this.state.itemsCount,
                breed: this.state.breed,
                age: this.state.age,
                gender: this.state.gender,
                color: this.state.color
            });
            return "ok";
        }
        else {
            return "invalid data";
        }

    }

    addToTasks() {
        if(this.state.description !== 'task description') {
            if(this.state.currentTask !== -1) {
                this.updateTask(this.state.currentTask);
                return "updated";
            }
            var index = this.isAlreadyTask(this.state.description);
            if(index !== -1) {
                return "already exists";
            }
            this.state.tasksCount = this.state.tasksCount + 1;
            this.state.tasks.push({
                id: this.state.currentPet,
                c: this.state.itemsCount,
                key: this.state.description
            });
            return "ok";
        }
        else {
            return "invalid data";
        }
    }

    updateTask(index) {
        var size = this.state.tasks.length;
        for(var i = 0; i < size; i++) {
            if(this.state.tasks[i].c === index) {
                this.state.tasks[i].key = this.state.description;
            }
        }
    }

    isAlreadyTask(description) {
        var tasksByID = this.getTasksByID();
        var size = tasksByID.length;

        for(var i = 0; i < size; i++) {
            if(tasksByID[i].key === description) {
                return tasksByID[i].c;
            }
        }

        return -1;
    }

    setStateAccordingToData(item) {
        this.state.name = item.key;
        this.state.breed = item.breed;
        this.state.age = item.age.toString();
        this.state.gender = item.gender;
        this.state.color = item.color;
    }

    setFormAccordingToData(item) {
        this.state.description = item.key;
        this.state.currentTask = item.c;
    }

  render() {
      this.state.itemsCount = this.state.pets.length;
      this.state.tasksCount = this.state.tasks.length;
      return (
          <View style={styles.container}>
              <ScrollView>
                  <FlatList
                      data={this.state.pets}
                      renderItem={
                          ({item}) => <View>
                              <Text style={{fontSize:20, color:"blue"}}>
                                  {item.key}, {item.breed}, {item.gender}</Text>
                              <Button
                                  title={"View/Edit"}
                                  onPress={() => {
                                      this.setmainModalVisible(!this.state.mainModalVisible);
                                      this.setStateAccordingToData(item);
                                  }}>
                              </Button>
                              <Button
                                title={"Tasks"}
                                onPress={() => {
                                    this.settaskModalVisible(!this.state.taskModalVisible);
                                    this.setCurrentPetByName(item.key);
                                }
                                }
                              >
                              </Button>

                                        </View>

                      }
                      extraData = {this.state}
                  >
                  </FlatList>
              </ScrollView>

              <Modal
                  animationType="slide"
                  transparent={false}
                  visible={this.state.mainModalVisible}
                  onRequestClose={() => {alert("Main Modal was closed"); this.setmainModalVisible(!this.state.mainModalVisible); this.resetState()}}
              >
                  <ScrollView>
                  <View style={{marginTop: 22}}>
                      <View>
                          <TextInput style={styles.inputText} onChangeText={(name) => this.setState({name})}
                                     value={this.state.name}
                          />
                          <TextInput style={styles.inputText} onChangeText={(breed) => this.setState({breed})}
                                     value={this.state.breed}
                          />
                          <TextInput style={styles.inputText} onChangeText={(age) => this.setState({age})}
                                     value={this.state.age}
                          />
                          <TextInput style={styles.inputText} onChangeText={(gender) => this.setState({gender})}
                                     value={this.state.gender}
                          />
                          <TextInput style={styles.inputText} onChangeText={(color) => this.setState({color})}
                                     value={this.state.color}
                          />
                          <Button
                          title={"Add new Pet/Edit existing Pet"}
                          onPress={() => {
                              this.setmainModalVisible(!this.state.mainModalVisible);
                              var result = this.addToPets();
                              if(result === "invalid data") {
                                  alert("New data should not contain input text placeholders.");
                              }
                              else if(result === "already exists") {
                                  this.updatePet(this.isInArray(this.state.name));
                                  alert("Updated pet with name " + this.state.name);
                              }
                              else {
                                  Communications.email(['nicodeni.pop96@gmail.com', 'nicodeni.pop96@gmail.com'],null,null,'New Pet ADDED-PetCave', this.getStateText());
                              }
                          }}>
                      </Button>

                      </View>
                  </View>
                  </ScrollView>
              </Modal>

              <Modal animationType="slide"
                     transparent={false}
                     visible={this.state.taskModalVisible}
                     onRequestClose={() => {alert("Task Modal was closed"); this.settaskModalVisible(!this.state.taskModalVisible); this.resetState()}}
              >
                  <View style={styles.container}>
                  <ScrollView>
                  <FlatList
                    style={{marginLeft:20, marginTop:20}}
                    data={this.getTasksByID()}
                    renderItem={
                        ({item}) =>
                            <View>
                                <Text style={{fontSize:20, color:"black"}}>
                                    {item.key}</Text>
                                <Button
                                title={"View/Edit"}
                                style={{width:10}}
                                onPress={() => {this.settaskDetailModalVisible(!this.state.taskDetailModalVisible);
                                                this.setFormAccordingToData(item)}}>

                                </Button>
                            </View>
                        }
                    extraData={this.state.tasks}
                  >
                  </FlatList>
                  </ScrollView>
                      <TouchableHighlight
                          style={styles.holder}
                          onPress={() => {
                              this.settaskDetailModalVisible(true)
                          }}>
                          <Text>Add Task Form</Text>
                      </TouchableHighlight>
                  </View>
              </Modal>

              <Modal
                  animationType="slide"
                  transparent={false}
                  visible={this.state.taskDetailModalVisible}
                  onRequestClose={() => {this.settaskDetailModalVisible(!this.state.taskDetailModalVisible); this.resetTask()}}
              >
                  <View
                      style={styles.container}>
                      <TextInput style={styles.inputText} onChangeText={(description) => this.setState({description})}
                                 value={this.state.description}
                      />
                      <Button
                          title={"Add new Task/Edit existing Task"}
                          onPress={() => {
                              this.settaskDetailModalVisible(!this.state.taskDetailModalVisible);

                              var result = this.addToTasks();

                              if(result === "invalid data") {
                                  alert("New data should not contain input text placeholders.");
                              }
                              else if(result === "already exists") {
                                  alert("This task already exists: " + this.state.description);
                              }
                              else if(result === "updated") {
                                  alert("task updated with new description: " + this.state.description);
                              }
                              else {
                                  alert("Added new task!");
                              }
                          }}>
                      </Button>
                  </View>
              </Modal>


              <TouchableHighlight
                  style={styles.holder}
                  onPress={() => {
                  this.setmainModalVisible(true);
              }}>
                  <Text>Add Pet Form</Text>
              </TouchableHighlight>

              <Text style={styles.welcomeText}>Welcome to PetCave!</Text>
          </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
      flex: 1,
      paddingTop:50,
      marginLeft: -60,
      backgroundColor: '#fff',
      alignItems: 'center',
      justifyContent: 'center',
  },
    welcomeText: {
        marginLeft:60,
    },
    inputText: {
      width: 300,
        marginBottom: 70,
        marginLeft: 40,
    },
    holder: {
        marginBottom: 40,
        marginLeft: 50,
    },
});
