FROM node:14
RUN npm install -g @aws-amplify/cli
WORKDIR /app
