
import Modal from 'react-bootstrap/Modal';
import Map from './Map';

function MapModal({show,setShow,setAddress,title="Enter your delivery address"}) {
 
  const handleClose = () => setShow(false);
  
  return (
    <>
      <Modal show={show} onHide={handleClose}>
        <Modal.Header closeButton>
          <Modal.Title>{title}</Modal.Title>
        </Modal.Header>
        <Map setAddress={setAddress} ></Map>
      </Modal>
    </>
  );
}

export default MapModal;