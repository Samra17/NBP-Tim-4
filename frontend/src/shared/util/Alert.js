import Toast from 'react-bootstrap/Toast'
import { ToastContainer } from 'react-bootstrap';
import { CheckCircleFill,XCircleFill,InfoCircleFill } from 'react-bootstrap-icons';


function CustomAlert({ type, msg={}, show, setShow }) {

    const getMessage = (message) => {
        if(message.hasOwnProperty("errors")) {
            return message.errors.map(i => <span>{i}<br></br></span>)
        } else if(message.hasOwnProperty("error")) {
            return message.error;
        }
        else {
            return message
        }
    }

    const variant = type=="error"? 'danger' : type=="success" ? "success" : "light"
    //Possible variants: primary, secondary, success, danger, warning, info, light, dark
    return (
        <>
            <ToastContainer className="p-3" position={'top-end'} style={{position:"fixed"}}>
                <Toast onClose={() => setShow(false)} animation show={show} bg={variant} delay={10000} autohide>
                    <Toast.Header>
                        {type =="error"?
                            <>
                            <XCircleFill className="text-danger" size={20}></XCircleFill>
                            <strong className="me-auto px-2 text-danger">Error</strong>
                            </>
                         : type== "success" ?
                         <>
                         <CheckCircleFill className="text-success" size={20}/>
                         <strong className="me-auto px-2 text-success" color='red'>Success</strong>
                         </>
                         : <>
                         <InfoCircleFill style={{color:"#272d2f"}} size={20}/>
                         <strong className="me-auto px-2 " style={{color:"#272d2f"}}>Notification</strong>
                         </>
                         }
                        
                    </Toast.Header>

                    <Toast.Body className={type?'text-white' : 'text-dark'}>
                        <div>
                       {getMessage(msg)}
                       </div>
                    </Toast.Body>
                </Toast>
            </ToastContainer >

        </>
    );
}

export default CustomAlert;