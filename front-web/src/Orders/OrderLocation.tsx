import { useState } from 'react';
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
import AsyncSelect from 'react-select/async';
import { OrderLocationData } from './types';

const initialPosition = {
    lat: -6.8607051,
    lng: -35.4967636
}

type Place = {
    label?: string;
    value?: string;
    position: {
        lat: number;
        lng: number;
    };
}

type Props = {
    onChangeLocation: (location: OrderLocationData) => void;
}

function OrderLocation({ onChangeLocation }: Props) {
    const [address, setAddress] = useState<Place>({
        position: initialPosition
    });

     const loadOptions = (inputValue: string, callback: (places: Place[]) => void) => {
        // const response = await fetchLocalMapBox(inputValue);
        // const places = response.data.features.map((item: any) => {
        //     return ({
        //         label: item.place_name,
        //         value: item.place_name,
        //         position: {
        //             lat: item.center[1],
        //             lng: item.center[0]
        //         }
        //     });
        // });
        const places = [
          {
              label: "Rio Tinto, Porto, Portugal",
              value: "Rio Tinto, Porto, Portugal",
              position: {
                  lat: 41.180717,
                  lng: -8.557569
              }
          },
          {
              label: "Rio Tinto, Paraíba, Brazil",
              value: "Rio Tinto, Paraíba, Brazil",
              position: {
                  lat: -6.803828,
                  lng: -35.077569
              }
          },
          {
              label: "Rio Tinto, St. Georges Terrace, Perth, Western Australia 6000, Australia",
              value: "Rio Tinto, St. Georges Terrace, Perth, Western Australia 6000, Australia",
              position: {
                  lat: -31.954183,
                  lng: 115.855906
              }
          },
          {
              label: "Calle Riotinto, 41928 Palomares del Río, Seville, Spain",
              value: "Calle Riotinto, 41928 Palomares del Río, Seville, Spain",
              position: {
                  lat: 37.3226260205204,
                  lng: -6.0711255202545
              }
          }
      ];

      callback(places);
    };

    const handleChangeSelect = (place: Place) => {
        setAddress(place);
        onChangeLocation({
            latitude: place.position.lat,
            longitude: place.position.lng,
            address: place.label!
        });
    };

    return (
        <div className="order-location-container">
            <div className="order-location-content">
                <h3 className="order-location-title">
                    Selecione onde o pedido deve ser entregue:
                </h3>
                <div className="filter-container">
                    <AsyncSelect
                        placeholder="Digite um endereço para entregar o pedido"
                        className="filter"
                        loadOptions={loadOptions}
                        onChange={value => handleChangeSelect(value as Place)}
                    />
                </div>
                <MapContainer
                    center={address.position}
                    zoom={13}
                    key={address.position.lat}
                    scrollWheelZoom
                >
                    <TileLayer
                        attribution='&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
                        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                    />
                    <Marker
                        position={address.position}
                    >
                        <Popup>
                            {address.label}
                        </Popup>
                    </Marker>
                </MapContainer>
            </div>
        </div>
    )
}

export default OrderLocation;