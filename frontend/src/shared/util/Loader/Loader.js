import { Fragment } from "react";
import { Spinner } from "react-bootstrap";
import "./Loader.css";

export function Loader({ isOpen, children }) {
  return (
    <>
      {isOpen ? (
        <>
          <Spinner className="spinner" />
          <Fragment>
            <div className="overlay">
              <div className="overlay__background" />

              {children}
            </div>
          </Fragment>
        </>
      ) : (
        <div>{children}</div>
      )}
    </>
  );
}

export default Loader;
