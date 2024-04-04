FROM node:20-alpine3.18 AS build
ENV VITE_BACKEND_URL=http://localhost:8080
ENV VITE_FRONTEND_URL=http://localhost
ENV HOME=/usr/app
RUN mkdir -p $HOME
WORKDIR $HOME
ADD . $HOME
RUN npm install
RUN npm run build

FROM nginx:1.25.4-alpine
COPY --from=build /usr/app/dist /usr/share/nginx/html
COPY nginx/nginx.conf /etc/nginx/nginx.conf
EXPOSE 80
ENTRYPOINT nginx -g 'daemon off;'
