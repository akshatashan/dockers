FROM python:3.6

WORKDIR /notebooks

COPY jupyter_lab_config.json /

RUN apt-get update
RUN apt-get -y install curl gnupg

# Install Node.js
RUN curl -sL https://deb.nodesource.com/setup_11.x  | bash -
RUN apt-get -y install nodejs
RUN npm install

RUN pip install jupyter jupyterlab ipywidgets plotly kafka

RUN jupyter labextension install @jupyter-widgets/jupyterlab-manager
RUN jupyter labextension install @jupyterlab/plotly-extension
RUN jupyter labextension install plotlywidget

EXPOSE 8888

CMD jupyter lab --config /jupyter_lab_config.json
