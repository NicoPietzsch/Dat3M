#Download base image ubuntu 20.04
FROM ubuntu:20.04

ARG DEBIAN_FRONTEND=noninteractive

# Update Ubuntu Software repository
RUN apt-get -qq update && \
    apt-get -qq upgrade -y && \
    apt-get -qq install -y software-properties-common && \
    apt-get -qq install -y git && \
    apt-get -qq install -y graphviz && \
    apt-get -qq install -y sudo && \
    apt-get -qq install -y wget && \
    apt-get -qq install -y maven && \
    apt-get -qq install -y cmake && \
    apt-get -qq install -y g++ && \
    apt-get -qq install -y autoconf && \
    apt-get -qq install -y automake && \
    apt-get -qq install -y graphviz

# Install SMACK
RUN cd home && \
    git clone https://github.com/smackers/smack.git && \
    cd smack && \
    env TEST_SMACK=0 INSTALL_Z3=0 INSTALL_CORRAL=0 bin/build.sh

# Install Dat3M
RUN cd home && \
    git clone https://github.com/NicoPietzsch/Dat3M.git && \
    cd Dat3M && \
    chmod 755 Dartagnan-SVCOMP.sh && \
    mvn clean install -DskipTests

# Build atomic-replace library
RUN cd home/Dat3M/llvm-passes/atomic-replace/ \
    && mkdir build && cd build                \
    && cmake ..                               \
    && make all install

# symlink for clang
RUN ln -s clang-12 /usr/bin/clang

ENV DAT3M_HOME=/home/Dat3M
ENV DAT3M_OUTPUT=$DAT3M_HOME/output
ENV CFLAGS="-I$DAT3M_HOME/include"
ENV SMACK_FLAGS="-q -t --no-memory-splitting"
ENV ATOMIC_REPLACE_OPTS="-mem2reg -sroa -early-cse -indvars -loop-unroll -simplifycfg -gvn"
ENV LD_LIBRARY_PATH=/usr/local/lib