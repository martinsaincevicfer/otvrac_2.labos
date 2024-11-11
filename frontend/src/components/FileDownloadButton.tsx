import React from 'react';
import axios from 'axios';

interface FileDownloadButtonProps {
    fileName: string;
}

const FileDownloadButton: React.FC<FileDownloadButtonProps> = ({ fileName }) => {
    const handleDownload = async () => {
        try {
            const response = await axios.get(`http://localhost:8080/download/${fileName}`, {
                responseType: 'blob',
            });

            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', fileName);
            document.body.appendChild(link);
            link.click();

            document.body.removeChild(link);
            window.URL.revokeObjectURL(url);
        } catch (error) {
            console.error('Error downloading the file', error);
        }
    };

    return (
        <button onClick={handleDownload}>Download {fileName}</button>
    );
};

export default FileDownloadButton;
