################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../src/Car.cpp \
../src/Grid.cpp \
../src/GridPane.cpp \
../src/Map.cpp 

OBJS += \
./src/Car.o \
./src/Grid.o \
./src/GridPane.o \
./src/Map.o 

CPP_DEPS += \
./src/Car.d \
./src/Grid.d \
./src/GridPane.d \
./src/Map.d 


# Each subdirectory must supply rules for building sources it contributes
src/%.o: ../src/%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	g++ -O0 -g3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


