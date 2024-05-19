import React, {  useState } from 'react'
import { Form, Button, Row, Col } from 'react-bootstrap'
import { LocationOn } from '@mui/icons-material'
import authService from '../../service/auth.service'
import MapModal from '../../shared/MapModal/MapModal'
import CustomAlert from '../../shared/util/Alert'
import { useEffect } from 'react'
import Loader from '../../shared/util/Loader/Loader'

function UserInformation() {
    const [validated, setValidated] = useState()
    const [formData, setFormData] = useState()
    const [showMap, setShowMap] = useState(false)
    const [alert, setAlert] = useState({})
    const [showAlert, setShowAlert] = useState(false)
    const [loading, setLoading] = useState(true)
    var mounted = false;

    useEffect( ()=> {
        if(!mounted){
            mounted=true
            authService.getLoggedInUser().then(res=> {
                if(res.status == 200) {
                    setFormData(res.data)
                    setLoading(false)
                }
            }
            )
            
        }
    }, [])

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value })

    }

    const setAddress = (addr, coordinates) => {
        setFormData({ ...formData, address: addr, mapCoordinates: coordinates })


    }


    const handleSubmit = (e) => {
        const form = e.currentTarget;
        e.preventDefault();
        e.stopPropagation();
        if (form.checkValidity() === false) {

            return;
        }

        setValidated(true);

        if (formData.address != null && formData.address.length == 0) {
            var req = {
                id: formData.id,
                firstname: formData.firstname,
                lastname: formData.lastname,
                email: formData.email,
                username: formData.username,
                password: formData.password
            }
            if (formData.phoneNumber != null) {
                req.phoneNumber = formData.phoneNumber;
            }
        } else {
            var req = formData
        }
        try {
            document.body.style.cursor = "wait";
            authService.updateUserInformation(req).then(res => {
                if (res.status == 200) {
                    document.body.style.cursor = "default";
                    setAlert({ ...alert, msg: ["You have successfully changed your account information!"], type: "success" })
                    setShowAlert(true)
                    setFormData(res.data)

                } else {
                    document.body.style.cursor = "default";
                    setAlert({ ...alert, msg: res.data, type: "error" })
                    setShowAlert(true)
                }

            })
        } catch (e) {
            console.log(e)
        }
    }


    return (
        <>
            <MapModal show={showMap} setShow={setShowMap} setAddress={setAddress} ></MapModal>
            <CustomAlert setShow={setShowAlert} show={showAlert} type={alert.type} msg={alert.msg}></CustomAlert>
            {loading ? <Loader></Loader> : <></>}
            {formData ? (
            <Form validated={validated} onSubmit={e => handleSubmit(e)} style={{ padding: "20px" }}>
                <Form.Group className="mb-3" controlId="formGroupFirstname">
                    <Form.Label>Firstname</Form.Label>
                    <Form.Control required type="text" name="firstname" value={formData.firstname} onChange={handleChange} />
                </Form.Group>
                <Form.Group className="mb-3" controlId="formGroupLastname">
                    <Form.Label>Lastname</Form.Label>
                    <Form.Control required type="text" name="lastname" value={formData.lastname} onChange={handleChange} />
                </Form.Group>
                <Form.Group className="mb-3" controlId="formGroupUsername">
                    <Form.Label>Username</Form.Label>
                    <Form.Control required type="text" name="username" value={formData.username} onChange={handleChange} />
                </Form.Group>
                <Form.Group className="mb-3" controlId="formGroupEmail">
                    <Form.Label>Email address</Form.Label>
                    <Form.Control required type="email" name="email" value={formData.email} onChange={handleChange} />
                </Form.Group>
                <Form.Group className="mb-3" controlId="formGroupPassword">
                    <Form.Label>Password</Form.Label>
                    <Form.Control type="password" name="password" value={formData.password} onChange={handleChange} />
                </Form.Group>
                <Form.Group className="mb-3" controlId="formGroupAddress">
                    <Form.Label >Address (optional)</Form.Label>
                    <Row>
                        <Col xs={11}><Form.Control readonly disabled type="text" name="address" value={formData.address} /></Col>
                        <Col xs={1} className="p-0"><LocationOn className='clickable' onClick={() => setShowMap(true)}></LocationOn></Col>
                    </Row>
                </Form.Group>
                <Form.Group controlId="formGroupPhoneNumber" className="mb-3">
                    <Form.Label>Phone number (optional)</Form.Label>
                    <Form.Control onKeyPress={(event) => {
                        if (!/[0-9]/.test(event.key)) {
                            event.preventDefault();
                        }
                    }} size="sm" name="phoneNumber" value={formData.phoneNumber} onChange={handleChange} />

                </Form.Group>

                <Button type="submit" >
                    Submit changes
                </Button>
            </Form>
            ) : <></>}
        </>
    )
}

export default UserInformation
