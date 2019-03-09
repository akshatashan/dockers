import json
import requests
from sseclient import SSEClient as EventSource

url = 'https://stream.wikimedia.org/v2/stream/recentchange'
for event in EventSource(url):
    if event.event == 'message':
        try:
        	# -*- coding: utf-8 -*-
			data = event.data.encode('utf-8')
			change = json.loads(data)
			if change["type"] == 'edit':
				change = json.dumps(change).encode('utf-8')
				#json_payload = json.dumps(payload).encode('utf-8')
				response = requests.post("http://localhost:8080/entry/input", data=change, headers={"Content-type": "application/json"})
				if response.status_code == 200:
					print("sent {payload} to {topic}".format(payload=change, topic="input"))
        except ValueError:
			pass
